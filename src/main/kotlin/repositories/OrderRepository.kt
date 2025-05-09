package com.example.repositories

import com.example.models.entities.OrderEntity
import com.example.models.dtos.OrderDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

object OrderRepository {

    // Создание заказа
    fun create(
        userId: UUID,
        configToken: String,
        sum: Double,
        status: String = "new"
    ): UUID = transaction {
        OrderEntity.insert {
            it[OrderEntity.userId] = userId
            it[OrderEntity.configToken] = configToken
            it[OrderEntity.sum] = sum.toBigDecimal()
            it[OrderEntity.status] = status
            it[OrderEntity.createdAt] = Instant.now()
            it[OrderEntity.updatedAt] = Instant.now()
        }[OrderEntity.id].value
    }

    // Получение заказа по ID
    fun getById(id: UUID): OrderDto? = transaction {
        OrderEntity.select { OrderEntity.id eq id }
            .singleOrNull()
            ?.toOrderDto()
    }

    // Получение всех заказов пользователя
    fun getByUserId(userId: UUID): List<OrderDto> = transaction {
        OrderEntity.select { OrderEntity.userId eq userId }
            .map { it.toOrderDto() }
    }

    // Обновление статуса заказа
    fun updateStatus(orderId: UUID, newStatus: String) = transaction {
        OrderEntity.update({ OrderEntity.id eq orderId }) {
            it[status] = newStatus
            it[updatedAt] = Instant.now()
        }
    }

    // Перемещение в архив
    fun archive(orderId: UUID) = transaction {
        OrderEntity.update({ OrderEntity.id eq orderId }) {
            it[isArchived] = true
            it[updatedAt] = Instant.now()
        }
    }

    private fun ResultRow.toOrderDto() = OrderDto(
        id = this[OrderEntity.id].value,
        userId = this[OrderEntity.userId],
        configToken = this[OrderEntity.configToken],
        sum = this[OrderEntity.sum].toDouble(),
        status = this[OrderEntity.status],
        createdAt = this[OrderEntity.createdAt],
        updatedAt = this[OrderEntity.updatedAt]
    )
}