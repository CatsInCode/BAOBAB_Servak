
package com.example.database

import com.example.database.tokens.Tokens
import com.example.database.users.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/tst",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "1qazxswedc"
        )
        transaction {
            SchemaUtils.create(Users, Tokens)
        }
    }
}
