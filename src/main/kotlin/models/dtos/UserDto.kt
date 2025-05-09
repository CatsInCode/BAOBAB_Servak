package com.example.models.dtos

import java.util.*

data class UserDto(
    val id: UUID,
    val login: String,
    val role: String,
    val telegramUsername: String?,
    val rating: Float
)