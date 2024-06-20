package com.example.nutrix.models

data class ProgressNutrition(
    val status: Int,
    val message: String,
    val data: dataProgress
) data class dataProgress(
    val totalCalories: Float,
    val totalCarbohydrate: Float,
    val totalProtein: Float,
    val totalFat: Float,
    val totalSugar: Float,
)
