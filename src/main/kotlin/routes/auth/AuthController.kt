package com.example.routes.auth

import com.example.database.users.UserService
import com.example.database.users.AuthResponse
import com.example.database.users.LoginRequest
import com.example.database.users.RegisterRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class AuthController(private val userService: UserService) {
    fun configureRoutes(routing: Route) {  // Изменили параметр с Routing на Route
        routing.apply {
            route("/auth") {
                post("/register") {
                    val request = call.receive<RegisterRequest>()
                    try {
                        val user = userService.registerUser(request)
                        call.respond(
                            HttpStatusCode.Created,
                            AuthResponse(user, "User registered successfully")
                        )
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.Conflict,
                            mapOf("error" to "Username or email already exists")
                        )
                    }
                }

                post("/login") {
                    val request = call.receive<LoginRequest>()
                    val user = userService.loginUser(request)
                    if (user != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            AuthResponse(user, "Login successful")
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Invalid email or password")
                        )
                    }
                }
            }
        }
    }
}