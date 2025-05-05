
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
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
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
