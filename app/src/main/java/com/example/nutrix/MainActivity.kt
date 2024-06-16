package com.example.nutrix

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nutrix.models.CalorieRequest
import com.example.nutrix.models.CalorieResponse
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.util.Base64

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_SELECT = 2
    private val REQUEST_CAMERA_PERMISSION = 100
    private var capturedImageBitmap: Bitmap? = null
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        val progressKarbo = findViewById<CircularProgressBar>(R.id.progress_karbohidrat)
        val progressProtein = findViewById<CircularProgressBar>(R.id.progress_protein)
        val progressLemak = findViewById<CircularProgressBar>(R.id.progress_lemak)
        val progressKalori = findViewById<CircularProgressBar>(R.id.progress_kalori)
        val progressGula = findViewById<CircularProgressBar>(R.id.progress_gula)
        val btnUploadImage = findViewById<Button>(R.id.btn_upload_image)
        val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
        val btnRecomendation = findViewById<Button>(R.id.btn_recomendation)
        val txtResponse = findViewById<TextView>(R.id.txt_response)

        // Set progress for CircularProgressBar
        progressKarbo.apply {
            setProgressWithAnimation(65f, 3000) // =1s
            progressMax = 100f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressProtein.apply {
            setProgressWithAnimation(90f, 3000) // =1s
            progressMax = 100f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressLemak.apply {
            setProgressWithAnimation(30f, 3000) // =1s
            progressMax = 100f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressKalori.apply {
            setProgressWithAnimation(50f, 3000) // =1s
            progressMax = 100f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        progressGula.apply {
            setProgressWithAnimation(80f, 3000) // =1s
            progressMax = 100f
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        // Set onClickListener for btnUploadImage
        btnUploadImage.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> requestCameraPermission()
                    1 -> chooseFromGallery()
                }
            }
            builder.show()
        }

        // Set onClickListener for btnAnalyze
        btnAnalyze.setOnClickListener {
            capturedImageBitmap?.let { bitmap ->
                val base64Image = fileToBase64(bitmap)
                sendImageToServer(base64Image)
            } ?: run {
                showToast("Please capture or select an image first")
            }
        }

        // Set onClickListener for btnRecomendation
        btnRecomendation.setOnClickListener {
            showRecommendationDialog()
        }
    }

    // Method to send image to server
    private fun sendImageToServer(base64Image: String?) {
        val userId = "user123"  // Sesuaikan dengan userId yang sebenarnya

        // Validasi base64Image, jika null atau kosong, tidak perlu melakukan request
        if (base64Image.isNullOrEmpty()) {
            showToast("Base64 image data is empty or null")
            return
        }

        showProgressDialog("Analyzing food...")

        val calorieRequest = CalorieRequest(userId, base64Image)

        val api = RetrofitClient.instance
        api.calorieTracker(calorieRequest).enqueue(object : Callback<CalorieResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CalorieResponse>,
                response: Response<CalorieResponse>
            ) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val calorieResponse = response.body()
                    if (calorieResponse != null && calorieResponse.foodInfo != null) {
                        val txtResponse = findViewById<TextView>(R.id.txt_response)
                        txtResponse.text = "Food: ${calorieResponse.foodInfo.foodName}\n" +
                                "Calories: ${calorieResponse.foodInfo.calorie}\n" +
                                "Carbs: ${calorieResponse.foodInfo.carbohydrate}\n" +
                                "Fat: ${calorieResponse.foodInfo.fat}\n" +
                                "Protein: ${calorieResponse.foodInfo.protein}\n" +
                                "Sugar: ${calorieResponse.foodInfo.sugar}"
                        txtResponse.visibility = View.VISIBLE
                    } else {
                        showToast("Failed to get valid response data")
                    }
                } else {
                    dismissProgressDialog()
                    // Handle different HTTP error codes and display corresponding messages
                    val errorMessage = when (response.code()) {
                        401 -> "Unauthorized: You are not authorized to access this resource"
                        404 -> "Not Found: The requested resource was not found"
                        500 -> "Internal Server Error: There was a problem with the server"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<CalorieResponse>, t: Throwable) {
                dismissProgressDialog()
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showProgressDialog(message: String) {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    // Method to show toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Method to request camera permission
    @SuppressLint("MissingPermission")
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePhoto()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    // Method to launch camera for taking photo
    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            showToast("No camera app available")
        }
    }

    // Method to choose image from gallery
    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_SELECT)
    }

    // Deprecated in Java, but necessary for onActivityResult in Kotlin
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            capturedImageBitmap = bitmap

            val imgView = findViewById<ImageView>(R.id.img_view)
            imgView.setImageBitmap(bitmap)
            imgView.visibility = View.VISIBLE

            val imgString = fileToBase64(capturedImageBitmap!!)
            val dataImageString = "data: ${imgString}, mimeType: image/jpg"

            val txtResponse = findViewById<TextView>(R.id.txt_response)
            txtResponse.text = dataImageString

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

    // Method to handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                showToast("Camera permission is required")
            }
        }
    }

    // Method to show recommendation dialog
    private fun showRecommendationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle("Recommendation")
            setMessage("Gilang ganteng.")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Method to convert Bitmap to Base64 string
    private fun fileToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    //    MENU PROFILE
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
