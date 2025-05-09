package com.example.models.dtos

import java.time.Instant

data class ConfigDto(
    val token: String,
    val components: String, // JSON
    val totalPrice: Double,
    val createdAt: Instant
)