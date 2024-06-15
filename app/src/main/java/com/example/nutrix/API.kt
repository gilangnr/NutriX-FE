package com.example.nutrix

import com.example.nutrix.models.LoginRequest
import com.example.nutrix.models.LoginResponse
import com.example.nutrix.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>


}