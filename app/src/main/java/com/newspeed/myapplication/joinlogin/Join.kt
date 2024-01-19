package com.newspeed.myapplication.joinlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.SignupRequest
import com.newspeed.myapplication.SignupResponse
import com.newspeed.myapplication.TestHottopic
import com.newspeed.myapplication.databinding.ActivityJoinBinding
import com.newspeed.myapplication.saveAuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Join : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnnext.setOnClickListener {
            val email = binding.joinEmail.text.toString().trim()
            val id = binding.joinId.text.toString().trim()
            val pw = binding.joinPw.text.toString().trim()

            // Modified: Get progress values from SeekBars
            val politics = binding.seekbarPolitics.progress
            val economy = binding.seekbarEconomy.progress
            val society = binding.seekbarSociety.progress
            val culture = binding.seekbarCulture.progress
            val science = binding.seekbarScience.progress
            val world = binding.seekbarWorld.progress


            // 수정된 부분: SignupRequest 객체 생성
            val signupRequest = SignupRequest(
                email = email,
                id = id,
                passwd = pw,
                category = listOfNotNull(
                    politics, economy, society, culture, science, world)
            )


            // 수정된 부분: 회원가입 API 호출
            val call = apiService.registerUser(signupRequest)
            call.enqueue(object : Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful) {
                        // 응답 처리
                        val signupResponse = response.body()
                        if (signupResponse != null) {
                            // 성공 처리
                            val intent = Intent(this@Join, Join2::class.java)
                            startActivity(intent)
                            Toast.makeText(this@Join, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            // 응답이 null인 경우에 대한 처리
                            Toast.makeText(applicationContext, "서버 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // 실패 처리
                        Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    val intent = Intent(this@Join, Join2::class.java)
                    startActivity(intent)
                    // 오류로 인한 실패 처리
                    Log.d("fail login", t.message.toString())
                    Toast.makeText(applicationContext, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("your_shared_pref_name", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_token_key", token)
        editor.apply()
    }
}
