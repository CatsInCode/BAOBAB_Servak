
package com.example.features.login

import com.example.models.LoginReceiveRemote
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.loginRouting() {
    post("/login") {
        val receive = call.receive<LoginReceiveRemote>()
        try {
            val response = LoginController().loginUser(receive)
            call.respond(response)
        } catch (e: Exception) {
            call.respond(e.message ?: "Unknown error")
        }
    }
}
