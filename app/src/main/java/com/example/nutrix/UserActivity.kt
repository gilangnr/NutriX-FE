package com.example.nutrix

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrix.models.Profile
import com.example.nutrix.models.ProfileResponse
import com.example.nutrix.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        fecthUserData()
        fetchUserProfile()
    }

    private fun fecthUserData() {
        val api = RetrofitClient.instance
        api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        val firstUser = it.firstOrNull()
                        if (firstUser != null) {
                            txtFullname.text = firstUser.name
                            txtEmail.text = firstUser.email
                        }
                    }
                } else {
                    txtFullname.text = "Gagal mendapatkan data"
                    txtEmail.text = "Gagal mendapatkan data"
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                txtFullname.text = "Gagal mendapatkan data"
                txtEmail.text = "Gagal mendapatkan data"
            }

        })
    }

    private fun fetchUserProfile() {
        val userId = "d5790195-555d-42f1-807d-9752667e7fc2"
        val api = RetrofitClient.instance

        api.getProfiles(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profileResponse = response.body()
                    val profile = profileResponse?.data // Assuming `data` is a property of ProfileResponse

                    profile?.let { actualProfile ->
                        txtGender.text = actualProfile.gender ?: "Data not available"
                        txtDob.text = actualProfile.dateOfBirth ?: "Data not available"
                        txtAllergies.text = actualProfile.allergies ?: "Data not available"
                        txtWeight.text = actualProfile.weight.toString() ?: "Data not available"
                        txtHeight.text = actualProfile.height.toString() ?: "Data not available"
                    }
                } else {
                    handleErrorResponse()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                handleErrorResponse()
            }

            private fun handleErrorResponse() {
                txtGender.text = "Gagal mendapatkan data"
                txtDob.text = "Gagal mendapatkan data"
                txtAllergies.text = "Gagal mendapatkan data"
                txtWeight.text = "Gagal mendapatkan data"
                txtHeight.text = "Gagal mendapatkan data"
            }
        })
    }


}