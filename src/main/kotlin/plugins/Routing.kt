package com.example.plugins

import com.example.routes.auth.authRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        authRoutes()
    }
}