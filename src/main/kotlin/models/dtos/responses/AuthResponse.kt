package com.example.models.dtos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)