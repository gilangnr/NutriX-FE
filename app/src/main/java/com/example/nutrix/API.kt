package com.example.nutrix

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("users")
    fun getUsers(): Call<List<User>>
}