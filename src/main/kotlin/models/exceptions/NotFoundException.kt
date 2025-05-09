package com.example.models.exceptions

import io.ktor.http.*

/**
 * Исключение для случаев, когда сущность не найдена
 * @property entityName Название сущности (например "User", "Order")
 * @property id ID, по которому осуществлялся поиск
 */
class NotFoundException(
    val entityName: String,
    val id: Any? = null
) : RuntimeException(
    "$entityName${id?.let { " with id $it" } ?: ""} not found"
) {
    val statusCode: HttpStatusCode = HttpStatusCode.NotFound
}