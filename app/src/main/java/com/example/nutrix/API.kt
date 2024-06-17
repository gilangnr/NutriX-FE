package com.example.nutrix

import com.example.nutrix.models.CalorieRequest
import com.example.nutrix.models.CalorieResponse
import com.example.nutrix.models.CreateProfileRequest
import com.example.nutrix.models.CreateProfileResponse
import com.example.nutrix.models.LoginRequest
import com.example.nutrix.models.LoginResponse
import com.example.nutrix.models.RegisterRequest
import com.example.nutrix.models.RegisterResponse
import com.example.nutrix.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface API {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @PATCH("food/calorie")
    fun calorieTracker(@Body request: CalorieRequest): Call<CalorieResponse>

    @POST("auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("profile/")
    fun createProfile(@Body request: CreateProfileRequest): Call<CreateProfileResponse>
}