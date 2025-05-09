package com.example

import com.example.config.DatabaseConfig
import com.example.config.JwtConfig
import com.example.config.PasswordConfig
import com.example.controllers.AuthController
import com.example.controllers.ConfigController
import com.example.controllers.OrderController
import com.example.controllers.UserController
import com.example.models.exceptions.NotFoundException
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureApplication()
    }.start(wait = true)
}

fun Application.configureApplication() {
    // Инициализация конфигураций
    DatabaseConfig.init()
    PasswordConfig.init()

    // Установка плагинов
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }

    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message))
        }
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "Internal server error: ${cause.message}")
            )
        }
    }

    install(Authentication) {
        jwt("jwt") {
            verifier(JwtConfig.verifier)
            validate { credential ->
                credential.payload.getClaim("userId").asString()?.let { userId ->
                    JWTPrincipal(credential.payload)
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid token"))
            }
        }
    }

    install(StatusPages) {
        exception<AuthException> { call, cause ->
            call.respond(cause.statusCode, mapOf("error" to cause.message))
        }

        exception<NotFoundException> { call, cause ->
            call.respond(cause.statusCode, mapOf(
                "error" to cause.message,
                "entity" to cause.entityName,
                "id" to cause.id
            ))
        }
    }

    // Маршрутизация
    routing {
        AuthController(this).apply {
            registerRoutes()
        }

        authenticate("jwt") {
            OrderController(this).apply {
                registerRoutes()
            }
            UserController(this).apply {
                registerRoutes()
            }
            ConfigController(this).apply {
                registerRoutes()
            }
        }
    }
}