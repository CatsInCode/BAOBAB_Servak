package com.example.controllers

import com.example.config.JwtConfig
import com.example.config.PasswordConfig
import com.example.models.dtos.requests.LoginRequest
import com.example.models.dtos.requests.RegisterRequest
import com.example.models.dtos.responses.AuthResponse
import com.example.models.exceptions.AuthException
import com.example.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

class AuthController(private val route: Route) {

    private val userRepository = UserRepository()

    init {
        route.apply {
            // Регистрация нового пользователя
            post("/register") {
                val request = call.receive<RegisterRequest>()

                // Проверка, что пользователь не существует
                if (userRepository.getByLogin(request.login) != null) {
                    throw AuthException("User already exists")
                }

                // Хеширование пароля
                val hashedPassword = PasswordConfig.hash(request.password)

                // Создание пользователя
                val userId = userRepository.create(
                    login = request.login,
                    passwordHash = hashedPassword,
                    telegramUsername = request.telegramUsername
                )

                // Генерация JWT токена
                val token = JwtConfig.generateToken(userId)

                call.respond(AuthResponse(token))
            }

            // Аутентификация пользователя
            post("/login") {
                val request = call.receive<LoginRequest>()
                val user = userRepository.getByLogin(request.login)
                    ?: throw AuthException("Invalid credentials")

                // Проверка пароля
                if (!PasswordConfig.verify(request.password, user.passwordHash)) {
                    throw AuthException("Invalid credentials")
                }

                // Генерация JWT токена
                val token = JwtConfig.generateToken(user.id)

                call.respond(AuthResponse(token))
            }
        }
    }
}