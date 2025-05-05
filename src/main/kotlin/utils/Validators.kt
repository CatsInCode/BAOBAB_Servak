package com.example.utils

object Validators {
    fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        return regex.matches(email)
    }
}
