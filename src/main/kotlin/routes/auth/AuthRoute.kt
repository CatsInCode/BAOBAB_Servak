package com.example.routes.auth

import com.example.database.users.UserService
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val userService = UserService()
    val authController = AuthController(userService)

    authController.configureRoutes(this)  // Передаем текущий Route
}