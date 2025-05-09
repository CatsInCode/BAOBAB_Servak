package com.example.models.entities

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.util.*

object UserEntity : UUIDTable("users") {
    val login = varchar("login", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 60)
    val role = varchar("role", 10).check { it.inList(listOf("user", "moder", "admin")) }
    val isBanned = bool("is_banned").default(false)
    val telegramUsername = varchar("telegram_username", 50).nullable()
    val ratingTotal = float("rating_total").default(0f)
    val ratingCount = integer("rating_count").default(0)
    val createdAt = timestamp("created_at").clientDefault { java.time.Instant.now() }

    init {
        index(true, login)
    }
}