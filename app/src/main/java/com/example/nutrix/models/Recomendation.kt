package com.example.nutrix.models

data class Recomendation(
    val status: Int,
    val message: String,
    val data: DataFood
)
data class DataFood(
    val food1: Food,
    val food2: Food,
    val food3: Food
)
data class Food(
    val foodName: String,
    val information: String
)
