package com.example.nutrix

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.nutrix.models.NutririonMax
import com.example.nutrix.models.ProgressNutrition
import com.example.nutrix.models.Recomendation

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
        val btnUploadImage = findViewById<Button>(R.id.btn_upload_image)
        val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
        val btnRecomendation = findViewById<Button>(R.id.btn_recomendation)

        // Set progress for CircularProgressBar
        setNutritionProgressBarMax()

        setProgressNutrition()

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
            showRecommendationDialog("d5790195-555d-42f1-807d-9752667e7fc2")
        }
    }

    private fun setProgressNutrition() {
        val userId = "d5790195-555d-42f1-807d-9752667e7fc2"//ganti ger
        val api = RetrofitClient.instance

        api.getProgressNutrition(userId).enqueue(object : Callback<ProgressNutrition> {
            override fun onResponse(
                call: Call<ProgressNutrition>,
                response: Response<ProgressNutrition>
            ) {
                if (response.isSuccessful) {
                    val nutrition = response.body()
                    if (nutrition != null) {
                        val progressKarbo =
                            findViewById<CircularProgressBar>(R.id.progress_karbohidrat)
                        val progressProtein =
                            findViewById<CircularProgressBar>(R.id.progress_protein)
                        val progressLemak = findViewById<CircularProgressBar>(R.id.progress_lemak)
                        val progressKalori = findViewById<CircularProgressBar>(R.id.progress_kalori)
                        val progressGula = findViewById<CircularProgressBar>(R.id.progress_gula)

                        val txtProgKarbo = findViewById<TextView>(R.id.txt_prog_karbohidrat)
                        val txtProgProtein = findViewById<TextView>(R.id.txt_prog_protein)
                        val txtProgLemak = findViewById<TextView>(R.id.txt_prog_lemak)
                        val txtProgKalori = findViewById<TextView>(R.id.txt_prog_kalori)
                        val txtProgGula = findViewById<TextView>(R.id.txt_prog_gula)

                        progressKarbo.progress = nutrition.data.totalCarbohydrate
                        progressProtein.progress = nutrition.data.totalProtein
                        progressLemak.progress = nutrition.data.totalFat
                        progressKalori.progress = nutrition.data.totalCalories
                        progressGula.progress = nutrition.data.totalSugar

                        txtProgKarbo.text = nutrition.data.totalCarbohydrate.toString() + "/"
                        txtProgProtein.text = nutrition.data.totalProtein.toString() + "/"
                        txtProgLemak.text = nutrition.data.totalFat.toString() + "/"
                        txtProgKalori.text = nutrition.data.totalCalories.toString() + "/"
                        txtProgGula.text = nutrition.data.totalSugar.toString() + "/"
                    }
                } else {
                    showToast("Gagal mendapatkan data progress nutrisi")
                }
            }

            override fun onFailure(call: Call<ProgressNutrition>, t: Throwable) {
                showToast("Error: ${t.message}")
            }

        })
    }

    private fun setNutritionProgressBarMax() {
        val userId = "d5790195-555d-42f1-807d-9752667e7fc2"//ganti ger
        val api = RetrofitClient.instance
        api.getTotalNutrition(userId).enqueue(object : Callback<NutririonMax> {
            override fun onResponse(call: Call<NutririonMax>, response: Response<NutririonMax>) {
                if (response.isSuccessful) {
                    val nutrition = response.body()
                    if (nutrition != null) {
                        val progressKarbo =
                            findViewById<CircularProgressBar>(R.id.progress_karbohidrat)
                        val progressProtein =
                            findViewById<CircularProgressBar>(R.id.progress_protein)
                        val progressLemak = findViewById<CircularProgressBar>(R.id.progress_lemak)
                        val progressKalori = findViewById<CircularProgressBar>(R.id.progress_kalori)
                        val progressGula = findViewById<CircularProgressBar>(R.id.progress_gula)

                        val txtMaxKarbo = findViewById<TextView>(R.id.txt_max_karbohidrat)
                        val txtMaxProtein = findViewById<TextView>(R.id.txt_max_protein)
                        val txtMaxLemak = findViewById<TextView>(R.id.txt_max_lemak)
                        val txtMaxKalori = findViewById<TextView>(R.id.txt_max_kalori)
                        val txtMaxGula = findViewById<TextView>(R.id.txt_max_gula)

                        progressKarbo.progressMax = nutrition.data.carbohydrate
                        progressProtein.progressMax = nutrition.data.proteins
                        progressLemak.progressMax = nutrition.data.fat
                        progressKalori.progressMax = nutrition.data.calories
                        progressGula.progressMax = nutrition.data.sugar

                        txtMaxKarbo.text = nutrition.data.carbohydrate.toString()
                        txtMaxProtein.text = nutrition.data.proteins.toString()
                        txtMaxLemak.text = nutrition.data.fat.toString()
                        txtMaxKalori.text = nutrition.data.calories.toString()
                        txtMaxGula.text = nutrition.data.sugar.toString()

                    }
                } else {
                    showToast("Gagal mendapatkan data max nutrisi")
                }
            }

            override fun onFailure(call: Call<NutririonMax>, t: Throwable) {
                showToast("Error: ${t.message}")
            }

        })
    }

    // Method to send image to server
    private fun sendImageToServer(base64Image: String?) {
        val userId = "6f1a1761-58c4-46fd-afe2-33ffc2ae4c81"  // Sesuaikan dengan userId yang sebenarnya

        // Validasi base64Image, jika null atau kosong, tidak perlu melakukan request
        if (base64Image.isNullOrEmpty()) {
            showToast("Base64 image data is empty or null")
            return
        }

        showProgressDialog("Analyzing food...")
        val calorieRequest = CalorieRequest(base64Image)

        val api = RetrofitClient.instance
        api.calorieTracker(userId, calorieRequest).enqueue(object : Callback<CalorieResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CalorieResponse>,
                response: Response<CalorieResponse>
            ) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    val calorieResponse = response.body()
                    Log.d("response body", response.body().toString())
                    if (calorieResponse != null) {
                        val txtResponse = findViewById<TextView>(R.id.txt_response)
                        txtResponse.visibility = View.VISIBLE
                        val foodInfo = calorieResponse.data.foodInfo
                        val totalNutrition = calorieResponse.data.totalNutrition

                        txtResponse.text = "Food Name: ${foodInfo.foodName}\n" +
                                "Calories: ${foodInfo.calorie} kcal\n" +
                                "Protein: ${foodInfo.protein} g\n" +
                                "Carbohydrates: ${foodInfo.carbohydrate} g\n" +
                                "Fats: ${foodInfo.fat} g\n" +
                                "Sugar: ${foodInfo.sugar} g\n\n" +
                                "Total Nutrition:\n" +
                                "Total Calories: ${totalNutrition.totalCalories} kcal\n" +
                                "Total Carbohydrates: ${totalNutrition.totalCarbohydrate} g\n" +
                                "Total Protein: ${totalNutrition.totalProtein} g\n" +
                                "Total Fats: ${totalNutrition.totalFat} g\n" +
                                "Total Sugar: ${totalNutrition.totalSugar} g"

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
    @RequiresApi(Build.VERSION_CODES.P)
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

//            val txtResponse = findViewById<TextView>(R.id.txt_response)
//            txtResponse.text = dataImageString

            val btnAnalyze = findViewById<Button>(R.id.btn_analyze)
            btnAnalyze.visibility = View.VISIBLE
        } else if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val imgView = findViewById<ImageView>(R.id.img_view)
            imgView.setImageURI(uri)
            imgView.visibility = View.VISIBLE

            // Convert selected image to Bitmap
            val source = ImageDecoder.createSource(this.contentResolver, uri!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            capturedImageBitmap = bitmap

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
    private fun showRecommendationDialog(userId: String) {
        val api = RetrofitClient.instance
        api.getRecomendation(userId).enqueue(object : Callback<Recomendation> {
            override fun onResponse(call: Call<Recomendation>, response: Response<Recomendation>) {
                if (response.isSuccessful) {
                    val recommendation = response.body()
                    if (recommendation != null) {
                        val message = """
                        #1 ${recommendation.data.food1.foodName} - ${recommendation.data.food1.information}
                        
                        #2 ${recommendation.data.food2.foodName} - ${recommendation.data.food2.information}
                        
                        #3 ${recommendation.data.food3.foodName} - ${recommendation.data.food3.information}
                    """.trimIndent()

                        val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                        alertDialogBuilder.apply {
                            setTitle("Recommendation")
                            setMessage(message)
                            setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                        }

                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    } else {
                        showToast("Failed to get recommendation data")
                    }
                } else {
                    showToast("Failed to get recommendation data")
                }
            }

            override fun onFailure(call: Call<Recomendation>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Method to convert Bitmap to Base64 string
    private fun fileToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
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
