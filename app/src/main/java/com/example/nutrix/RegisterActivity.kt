package com.example.nutrix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var moveLogin: TextView
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editGender: EditText
    private lateinit var editDOB: EditText
    private lateinit var editAllergies: EditText
    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        btnRegister = findViewById(R.id.btn_register)
        moveLogin = findViewById(R.id.move_login)
        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        editGender = findViewById(R.id.edit_gender)
        editDOB = findViewById(R.id.edit_dob)
        editAllergies = findViewById(R.id.edit_allergies)
        editWeight = findViewById(R.id.edit_weight)
        editHeight = findViewById(R.id.edit_height)

        btnRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        moveLogin.setOnClickListener { finish() }
    }
}