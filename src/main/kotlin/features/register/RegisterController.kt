package com.example.features.register

import com.example.database.tokens.Tokens
import com.example.database.users.Users
import com.example.models.RegisterReceiveRemote
import com.example.models.RegisterResponseRemote
import com.example.utils.Validators
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RegisterController {
    fun registerUser(receive: RegisterReceiveRemote): RegisterResponseRemote {
        // Валидация email
        if (!Validators.isValidEmail(receive.email)) {
            throw IllegalArgumentException("Invalid email")
        }

        // Проверка, что username и password не пустые
        if (receive.login.isNullOrBlank()) {
            throw IllegalArgumentException("Username cannot be null or empty")
        }
        if (receive.password.isNullOrBlank()) {
            throw IllegalArgumentException("Password cannot be null or empty")
        }
        if (receive.username.isNullOrBlank()) {
            throw IllegalArgumentException("Full name (username) cannot be null or empty")
        }

        transaction {
            // Проверка, существует ли уже пользователь с таким login
            val existing = Users.select { Users.login eq receive.login }.singleOrNull()
            if (existing != null) throw IllegalArgumentException("User already exists")

            // Вставка нового пользователя
            Users.insert {
                it[login] = receive.login
                it[password] = receive.password
                it[username] = receive.username // Добавлено поле username
                it[email] = receive.email
            }

            // Генерация токена
            val token = UUID.randomUUID().toString()

            // Вставка токена в таблицу Tokens
            Tokens.insert {
                it[login] = receive.login
                it[Tokens.token] = token
            }
        }

        // Извлечение токена
        val token = transaction {
            Tokens.select { Tokens.login eq receive.login }.single()[Tokens.token]
        }

        // Возврат ответа с токеном
        return RegisterResponseRemote(token = token)
    }
}
