package com.example.nutrix.models

data class CreateProfileResponse(
    val status: Int,
    val message: String,
    val data: ProfileData
)
