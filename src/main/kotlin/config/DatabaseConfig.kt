package com.example.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object DatabaseConfig {
    // Настройки подключения из переменных окружения
    private val dbUrl: String = System.getenv("JDBC_DATABASE_URL")
        ?: "jdbc:postgresql://localhost:5432/pc_orders"

    private val dbUser: String = System.getenv("DB_USER")
        ?: "postgres"

    private val dbPassword: String = System.getenv("DB_PASSWORD")
        ?: "postgres"

    // Пул соединений HikariCP
    private val dataSource: DataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = dbUrl
        username = dbUser
        password = dbPassword
        maximumPoolSize = 5
        minimumIdle = 1
        driverClassName = "org.postgresql.Driver"
        poolName = "pc-orders-pool"
        validate()
    })

    // Инициализация подключения
    fun init() {
        Database.connect(dataSource)

        // Тестовый запрос для проверки подключения
        transaction {
            addLogger(StdOutSqlLogger) // Логирование SQL в консоль (для разработки)
            exec("SELECT 1") // Проверка что БД отвечает
            println("✅ Database connection established")
        }
    }

    // Для ручного управления транзакциями
    fun <T> query(block: () -> T): T = transaction {
        addLogger(StdOutSqlLogger)
        block()
    }
}