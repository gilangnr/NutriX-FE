package com.example.nutrix

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressKarbo = findViewById<ProgressBar>(R.id.progress_karbohidrat)
        val progressProtein = findViewById<ProgressBar>(R.id.progress_protein)
        val progressLemak = findViewById<ProgressBar>(R.id.progress_lemak)
        val progressVitamin = findViewById<ProgressBar>(R.id.progress_vitamin)
        val progressMineral = findViewById<ProgressBar>(R.id.progress_mineral)

        progressKarbo.progress = 50
        progressProtein.progress = 70
        progressLemak.progress = 30
        progressVitamin.progress = 90
        progressMineral.progress = 45
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_page -> {
                startActivity(Intent(this, UserActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}