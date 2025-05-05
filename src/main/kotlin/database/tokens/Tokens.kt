
package com.example.database.tokens

import org.jetbrains.exposed.sql.Table

object Tokens : Table("tokens") {
    val login = varchar("login", 50)
    val token = varchar("token", 100)
}
