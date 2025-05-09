package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object ConfigEntity : Table("pc_configs") {
    val token = varchar("token", 50)
    val userId = reference("user_id", UserEntity)
    val components = text("components")
    val totalPrice = decimal("total_price", 10, 2)
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }  // Тип Instant

    override val primaryKey = PrimaryKey(token)
}