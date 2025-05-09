package com.example.models.entities

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object OrderEntity : UUIDTable("orders") {
    val userId = reference("user_id", UserEntity, onDelete = ReferenceOption.CASCADE)
    val configToken = varchar("config_token", 50)
    val sum = decimal("sum", 10, 2)
    val status = varchar("status", 20).check { it.inList(listOf("new", "processing", "done", "cancelled")) }
    val userRating = integer("user_rating").nullable()
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    val isArchived = bool("is_archived").default(false)

    init {
        index(false, userId)
        index(false, status)
    }
}