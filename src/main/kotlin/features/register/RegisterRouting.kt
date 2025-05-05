
package com.example.features.register

import com.example.models.RegisterReceiveRemote
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerRouting() {
    post("/register") {
        val receive = call.receive<RegisterReceiveRemote>()
        try {
            val response = RegisterController().registerUser(receive)
            call.respond(response)
        } catch (e: Exception) {
            call.respond(e.message ?: "Unknown error")
        }
    }
}
