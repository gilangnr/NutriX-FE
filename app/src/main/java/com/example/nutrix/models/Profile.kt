package com.example.nutrix.models

data class Profile(
    val id: String,
    val userId: String,
    val gender: String,
    val dateOfBirth: String,
    val allergies: String?,
    val weight: Float,
    val height: Float,
    val createdAt: String,
    val updatedAt: String,
    val user: user
) data class user(
    val name: String,
    val email: String
)
