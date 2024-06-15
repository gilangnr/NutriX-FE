package com.example.nutrix.models

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: User
)
