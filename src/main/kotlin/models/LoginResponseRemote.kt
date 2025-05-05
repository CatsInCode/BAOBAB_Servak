
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseRemote(
    val token: String
)
