package com.example.nutrix

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_SELECT = 2
    private val REQUEST_CAMERA_PERMISSION = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressKarbo = findViewById<CircularProgressBar>(R.id.progress_karbohidrat)
        val progressProtein = findViewById<CircularProgressBar>(R.id.progress_protein)
        val progressLemak = findViewById<CircularProgressBar>(R.id.progress_lemak)
        val progressKalori = findViewById<CircularProgressBar>(R.id.progress_kalori)
        val progressGula = findViewById<CircularProgressBar>(R.id.progress_gula)
        val btnUploadImage = findViewById<Button>(R.id.btn_upload_image)
        val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
        val txtResponse = findViewById<TextView>(R.id.txt_response)
        val btnRecomendation = findViewById<Button>(R.id.btn_recomendation)

        progressKarbo.apply {
            setProgressWithAnimation(65f, 3000) // =1s

            progressMax = 100f
            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressProtein.apply {
            setProgressWithAnimation(90f, 3000) // =1s

            progressMax = 100f
            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressLemak.apply {

            setProgressWithAnimation(30f, 3000) // =1s

            progressMax = 100f
            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressKalori.apply {
            setProgressWithAnimation(50f, 3000) // =1s

            progressMax = 100f
            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressGula.apply {
            setProgressWithAnimation(80f, 3000) // =1s

            progressMax = 100f
            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        btnUploadImage.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Select Option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> chooseFromGallery()
                }
            }
            builder.show()
        }

        btnAnalyze.setOnClickListener {
            val text = "Makanan kamu ******** nutrisi yang terkandung di makanan tsb adalah sebagai berikut, DATA NA MANA GERRY KOCAKK"

            txtResponse.text = text
            txtResponse.visibility = View.VISIBLE

        }

        btnRecomendation.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.apply {
                setTitle("Recommendation")
                setMessage("Gilang ganteng.")
                setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() // Menutup dialog
                }
            }

            // Membuat dialog dan menampilkannya
            val alertDialog = alertDialogBuilder.create()

            alertDialog.show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }
    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_SELECT)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            val imgView = findViewById<ImageView>(R.id.img_view)
            imgView.setImageBitmap(bitmap)
            imgView.visibility = View.VISIBLE

            val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
            btnAnalyze.visibility = View.VISIBLE
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val imgView = findViewById<ImageView>(R.id.img_view)
            imgView.setImageURI(uri)
            imgView.visibility = View.VISIBLE
            val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
            btnAnalyze.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()  // Launch camera intent if permission granted
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
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