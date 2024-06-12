package com.example.nutrix

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var moveRegister:TextView
    private lateinit var btnLogin: Button
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        moveRegister = findViewById(R.id.move_register)
        btnLogin = findViewById(R.id.btn_login)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)

        moveRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}