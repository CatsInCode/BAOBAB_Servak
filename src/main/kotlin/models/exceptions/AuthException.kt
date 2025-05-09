package com.example.models.exceptions

import io.ktor.http.*

/**
 * Исключение для ошибок аутентификации и авторизации
 * @property message Сообщение об ошибке
 * @property statusCode HTTP статус код (по умолчанию 401 Unauthorized)
 */
class AuthException(
    override val message: String,
    val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
) : RuntimeException(message)