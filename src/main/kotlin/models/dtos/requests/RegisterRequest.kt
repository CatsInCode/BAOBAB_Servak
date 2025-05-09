package com.example.models.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val telegramUsername: String? = null
)