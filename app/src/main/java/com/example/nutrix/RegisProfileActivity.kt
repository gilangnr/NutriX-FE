package com.example.nutrix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrix.models.CreateProfileRequest
import com.example.nutrix.models.CreateProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisProfileActivity : AppCompatActivity() {

    private lateinit var editGender: EditText
    private lateinit var editDOB: EditText
    private lateinit var editAllergies: EditText
    private lateinit var editWeight: EditText
    private lateinit var editHeight: EditText
    private lateinit var btnSendData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regis_profile)

        editGender = findViewById(R.id.edit_gender)
        editDOB = findViewById(R.id.edit_dob)
        editAllergies = findViewById(R.id.edit_allergies)
        editWeight = findViewById(R.id.edit_weight)
        editHeight = findViewById(R.id.edit_height)
        btnSendData = findViewById(R.id.btn_send_data)

        btnSendData.setOnClickListener {
            createProfile()
        }
    }

    private fun createProfile() {
        val gender = editGender.text.toString().trim()
        val dob = editDOB.text.toString().trim()
        val allergies = editAllergies.text.toString().trim()
        val weight = editWeight.text.toString().trim().toFloatOrNull() ?: 0f
        val height = editHeight.text.toString().trim().toFloatOrNull() ?: 0f
        val userId = "user-id-placeholder" // Ganti dengan ID pengguna yang sebenarnya

        if (gender.isEmpty() || dob.isEmpty() || weight <= 0 || height <= 0) {
            Toast.makeText(this, "Harap isi semua kolom dengan benar", Toast.LENGTH_SHORT).show()
            return
        }

        val createProfileRequest = CreateProfileRequest(userId, gender, dob, allergies, weight, height)

        RetrofitClient.instance.createProfile(createProfileRequest).enqueue(object : Callback<CreateProfileResponse> {
            override fun onResponse(
                call: Call<CreateProfileResponse>,
                response: Response<CreateProfileResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val createProfileResponse = response.body()!!
                    if (createProfileResponse.status == 200) {
                        Toast.makeText(this@RegisProfileActivity, "Profil berhasil dibuat", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisProfileActivity, createProfileResponse.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisProfileActivity, "Gagal membuat profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateProfileResponse>, t: Throwable) {
                Log.e("RegisProfileActivity", "Error: ${t.message}")
                Toast.makeText(this@RegisProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }


}