package com.example.nutrix.models

data class Nutrition(
    val id: String,
    val userId: String,
    val dailyCalorie: Float,
    val dailyCarbohydrate: Float,
    val dailySugar: Float,
    val dailyFat: Float,
    val dailyProtein: Float,
    val createdAt: String,
    val updatedAt: String
)
