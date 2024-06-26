package com.example.nutrix.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val isEmailVerified: Boolean
)