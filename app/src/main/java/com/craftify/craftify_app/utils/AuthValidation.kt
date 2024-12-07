package com.craftify.craftify_app.utils

import android.util.Patterns

object AuthValidation {
    /**
     * validate username format
     * @param username : String
     * @return Boolean
     */
    fun isUsernameValid(username : String) : Boolean {
        return username.isNotEmpty()
    }

    /**
     * validate email format
     * @param email : String
     * @return Boolean
     */
    fun isEmailValid(email : String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * validate password
     * @param password : String
     * @return Boolean
     */
    fun isPasswordValid(password : String) : Boolean {
        return password.length >= 6
    }

    /**
     * @param username : String
     * @param email : String
     * @param password : String
     * @return List<String>
     */
    fun validateAll(username: String, email: String, password: String): List<String> {
        val errors = mutableListOf<String>()

        if (username.isEmpty()) {
            errors.add("Please enter a username.")
        }

        if (email.isEmpty()) {
            errors.add("Please enter an email address.")
        } else if (!isEmailValid(email)) {
            errors.add("Invalid email format.")
        }

        if (password.isEmpty()) {
            errors.add("Please enter a password.")
        } else if (password.length < 6) {
            errors.add("Password must be at least 6 characters long.")
        }

        return errors
    }
}