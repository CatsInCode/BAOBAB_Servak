
package com.example

import com.example.database.DatabaseFactory
import com.example.features.login.loginRouting
import com.example.features.register.registerRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080  // Важно для Render
    embeddedServer(CIO, port = port, host = "0.0.0.0") {     // CIO → Netty для production
        install(ContentNegotiation) {
            json()
        }
        DatabaseFactory.init()
        routing {
            loginRouting()
            registerRouting()
        }
    }.start(wait = true)
}