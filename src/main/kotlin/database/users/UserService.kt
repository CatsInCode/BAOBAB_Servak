package com.example.database.users

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserService {
    fun registerUser(request: RegisterRequest): UserDTO {
        return transaction {
            val userId = Users.insert {
                it[username] = request.username
                it[email] = request.email
                it[password] = BCrypt.hashpw(request.password, BCrypt.gensalt())
                it[role] = "user"
                it[isBanned] = false
            } get Users.id

            UserDTO(
                id = userId,
                username = request.username,
                email = request.email,
                role = "user",
                isBanned = false
            )
        }
    }

    fun loginUser(request: LoginRequest): UserDTO? {
        return transaction {
            Users.select { Users.email eq request.email }.singleOrNull()?.let {
                val hashedPassword = it[Users.password]
                if (BCrypt.checkpw(request.password, hashedPassword)) {
                    UserDTO(
                        id = it[Users.id],
                        username = it[Users.username],
                        email = it[Users.email],
                        role = it[Users.role],
                        isBanned = it[Users.isBanned]
                    )
                } else {
                    null
                }
            }
        }
    }
}