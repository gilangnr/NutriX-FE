package com.example.nutrix.models

data class RegisterResponse(
    val status: Int,
    val message: String,
    val data: User
)
