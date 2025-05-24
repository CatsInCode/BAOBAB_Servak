package com.example.database

import com.example.database.users.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val host = "dpg-d0ckucodl3ps73ea4eug-a.frankfurt-postgres.render.com"
        val dbName = "db_crm_tst"
        val user = "admin"
        val password = "INIXJmdHrVqeXQb2tYlnYM83HxbNYDGe"

        Database.connect(
            url = "jdbc:postgresql://$host:5432/$dbName?sslmode=require",
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users)
        }
    }
}