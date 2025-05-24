package com.example.database.users

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 255)
    val role = varchar("role", 10)
    val isBanned = bool("is_banned").default(false)

    override val primaryKey = PrimaryKey(id)
}