package com.example.repositories

import com.example.models.entities.ConfigEntity
import com.example.models.dtos.ConfigDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

object ConfigRepository {

    // Создание конфигурации
    fun create(
        userId: UUID,
        components: String,
        totalPrice: Double
    ): String = transaction {
        val token = UUID.randomUUID().toString()
        ConfigEntity.insert {
            it[ConfigEntity.token] = token
            it[ConfigEntity.userId] = userId
            it[ConfigEntity.components] = components
            it[ConfigEntity.totalPrice] = totalPrice.toBigDecimal()
            it[ConfigEntity.createdAt] = Instant.now()  // Используем Instant вместо System.currentTimeMillis()
        }
        token
    }

    // Получение конфигурации по токену
    fun getByToken(token: String): ConfigDto? = transaction {
        ConfigEntity.select { ConfigEntity.token eq token }
            .singleOrNull()
            ?.toConfigDto()
    }

    private fun ResultRow.toConfigDto() = ConfigDto(
        token = this[ConfigEntity.token],
        components = this[ConfigEntity.components],
        totalPrice = this[ConfigEntity.totalPrice].toDouble(),
        createdAt = this[ConfigEntity.createdAt]  // Теперь возвращает Instant
    )
}