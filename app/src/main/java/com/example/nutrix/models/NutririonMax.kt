package com.example.nutrix.models

data class NutririonMax(
    val status: Int,
    val message: String,
    val data : NutritionData
) data class NutritionData(
    val calories: Float,
    val proteins: Float,
    val fat: Float,
    val carbohydrate: Float,
    val sugar: Float
)
