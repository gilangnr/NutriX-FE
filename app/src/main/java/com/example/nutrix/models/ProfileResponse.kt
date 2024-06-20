package com.example.nutrix.models

data class ProfileResponse(
    val status: Int,
    val message: String,
    val data: Profile
)
