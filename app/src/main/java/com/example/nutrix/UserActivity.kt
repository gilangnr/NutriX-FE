package com.example.nutrix

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private lateinit var back:ImageView
    private lateinit var txtFullname: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtGender: TextView
    private lateinit var txtDob: TextView
    private lateinit var txtAllergies: TextView
    private lateinit var txtWeight: TextView
    private lateinit var txtHeight: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        supportActionBar?.hide()

        back = findViewById(R.id.back)
        txtFullname = findViewById(R.id.txt_fullname)
        txtEmail = findViewById(R.id.txt_email)
        txtGender = findViewById(R.id.txt_gender)
        txtDob = findViewById(R.id.txt_dob)
        txtAllergies = findViewById(R.id.txt_allergies)
        txtWeight = findViewById(R.id.txt_weight)
        txtHeight = findViewById(R.id.txt_height)

        back.setOnClickListener { finish() }
    }
}