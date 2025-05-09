package com.example.models.dtos

import org.jetbrains.exposed.dao.id.EntityID
import java.time.Instant
import java.util.*

data class OrderDto(
    val id: UUID,
    val userId: EntityID<UUID>,
    val configToken: String,
    val sum: Double,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant
)