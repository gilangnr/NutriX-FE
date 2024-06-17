package com.example.nutrix.models

data class CreateProfileRequest(
    val userId: String,
    val gender: String,
    val dateOfBirth: String,
    val allergies: String?,
    val weight: Float,
    val height: Float
)
