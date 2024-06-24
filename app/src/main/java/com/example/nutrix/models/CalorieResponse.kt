package com.example.nutrix.models

data class CalorieResponse(
    val status: Int,
    val message: String,
    val data: dataResponse,
)

data class dataResponse(
    val foodInfo: FoodInfo,
    val totalNutrition: TotalNutrition
)

data class FoodInfo(
    val foodName: String,
    val calorie: Float,
    val carbohydrate: Float,
    val fat: Float,
    val protein: Float,
    val sugar: Float
)

data class TotalNutrition(
    val totalCalories: Float,
    val totalCarbohydrate: Float,
    val totalProtein: Float,
    val totalFat: Float,
    val totalSugar: Float
)

