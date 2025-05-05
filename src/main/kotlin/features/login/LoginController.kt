
package com.example.features.login

import com.example.database.tokens.Tokens
import com.example.database.users.Users
import com.example.models.LoginReceiveRemote
import com.example.models.LoginResponseRemote
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class LoginController {
    fun loginUser(receive: LoginReceiveRemote): LoginResponseRemote {
        val user = transaction {
            Users.select { Users.login eq receive.login }.singleOrNull()
        } ?: throw IllegalArgumentException("User not found")

        if (user[Users.password] != receive.password) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = transaction {
            Tokens.select { Tokens.login eq receive.login }.single()[Tokens.token]
        }

        return LoginResponseRemote(token = token)
    }
}
