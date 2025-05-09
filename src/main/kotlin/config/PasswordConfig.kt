package com.example.config

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordConfig {
    // Стоимость хеширования (4-31, чем больше - тем безопаснее, но медленнее)
    private const val BCRYPT_COST = 12

    /**
     * Хеширование пароля
     * @return BCrypt hash string
     */
    fun hash(password: String): String {
        return BCrypt.withDefaults()
            .hashToString(BCRYPT_COST, password.toCharArray())
    }

    /**
     * Верификация пароля
     * @return true если пароль совпадает с хешем
     */
    fun verify(password: String, bcryptHash: String): Boolean {
        return BCrypt.verifyer()
            .verify(password.toCharArray(), bcryptHash.toCharArray())
            .verified
    }
}