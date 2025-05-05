
package com.example.database.users

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val login = varchar("login", 50)
    val password = varchar("password", 64)
    val username = varchar("username", 64)
    val email = varchar("email", 100)
    override val primaryKey = PrimaryKey(login)
}
