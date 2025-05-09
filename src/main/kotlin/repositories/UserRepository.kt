package com.example.repositories

import com.example.models.entities.UserEntity
import com.example.models.dtos.UserDto
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object UserRepository {

    // Создание пользователя
    fun create(
        login: String,
        passwordHash: String,
        telegramUsername: String? = null,
        role: String = "user"
    ): UUID = transaction {
        UserEntity.insert {
            it[UserEntity.login] = login
            it[UserEntity.passwordHash] = passwordHash
            it[UserEntity.telegramUsername] = telegramUsername
            it[UserEntity.role] = role
        }[UserEntity.id].value
    }

    // Получение пользователя по логину
    fun getByLogin(login: String): UserDto? = transaction {
        UserEntity.select { UserEntity.login eq login }
            .singleOrNull()
            ?.toUserDto()
    }

    // Получение пользователя по ID
    fun getById(id: UUID): UserDto? = transaction {
        UserEntity.select { UserEntity.id eq id }
            .singleOrNull()
            ?.toUserDto()
    }

    // Обновление рейтинга пользователя
    fun updateRating(userId: UUID, newRating: Int) = transaction {
        UserEntity.update({ UserEntity.id eq userId }) {
            with(SqlExpressionBuilder) {
                it.update(ratingTotal, ratingTotal + newRating.toFloat())
                it.update(ratingCount, ratingCount + 1)
            }
        }
    }

    private fun ResultRow.toUserDto() = UserDto(
        id = this[UserEntity.id].value,
        login = this[UserEntity.login],
        role = this[UserEntity.role],
        telegramUsername = this[UserEntity.telegramUsername],
        rating = if (this[UserEntity.ratingCount] > 0)
            this[UserEntity.ratingTotal] / this[UserEntity.ratingCount]
        else 0f
    )
}