package com.example.nutrix

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val decorView = window.decorView
        // Hide the status bar.
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Hide ActionBar
        supportActionBar?.hide()
        // Timer
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
