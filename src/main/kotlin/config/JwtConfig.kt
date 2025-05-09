package com.example.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.server.application.*
import java.util.*

object JwtConfig {
    // Конфигурация из переменных окружения
    private const val SECRET_ENV = "JWT_SECRET"
    private const val ISSUER_ENV = "JWT_ISSUER"
    private const val AUDIENCE_ENV = "JWT_AUDIENCE"
    private const val EXPIRATION_DAYS_ENV = "JWT_EXPIRATION_DAYS"

    // Значения по умолчанию (для разработки)
    private val secret = System.getenv(SECRET_ENV) ?: "super_secret_key_123" // В продакшене обязательно через env!
    private val issuer = System.getenv(ISSUER_ENV) ?: "pc-orders-app"
    private val audience = System.getenv(AUDIENCE_ENV) ?: "users"
    private val expirationDays = System.getenv(EXPIRATION_DAYS_ENV)?.toLongOrNull() ?: 30L

    // Алгоритм подписи
    private val algorithm = Algorithm.HMAC256(secret)

    // Верификатор токенов
    val verifier: JWTVerifier? = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    /**
     * Генерация JWT токена
     * @param userId ID пользователя
     * @param additionalClaims Дополнительные claims (например, роль)
     */
    fun generateToken(userId: UUID, additionalClaims: Map<String, String> = emptyMap()): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId.toString())
            .apply {
                additionalClaims.forEach { (key, value) ->
                    withClaim(key, value)
                }
            }
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDays * 24 * 60 * 60 * 1000))
            .sign(algorithm)
    }

    /**
     * Валидация токена из запроса
     */
    fun validateToken(token: String): Boolean {
        return try {
            verifier?.verify(token)
            true
        } catch (ex: Exception) {
            false
        }
    }
}