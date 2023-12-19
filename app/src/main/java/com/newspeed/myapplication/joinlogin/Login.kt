package com.newspeed.myapplication.joinlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.LoginRequest
import com.newspeed.myapplication.LoginResponse
import com.newspeed.myapplication.TestHottopic
import com.newspeed.myapplication.databinding.ActivityLoginBinding
import com.newspeed.myapplication.saveAuthToken
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class Login : AppCompatActivity() {
    private lateinit var mbinding: ActivityLoginBinding
    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    private var loginRequest: LoginRequest = LoginRequest("", "")
    private var responseToken: LoginResponse? = null
    private var refreshToken: List<String> = emptyList()
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
        Log.d("goto", "login")

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.35.186:5001") // 실제 서버 주소로 수정
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(nullOnEmptyConverterFactory)
            .build()

        apiService = retrofit.create(ApiService::class.java)

        mbinding.btnlogin.setOnClickListener {
            val id = mbinding.loginId.text.toString().trim()
            val pw = mbinding.loginPw.text.toString().trim()

            loginRequest = LoginRequest(id = id, passwd = pw)

            val call = apiService.login(loginRequest)

            call.enqueue(object : Callback<LoginResponse>
            {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        Log.d("ServerResponse", "login Response success: $response")
                        responseToken = response.body()
                        refreshToken = response.headers().get("Set-Cookie")?.split(";") ?: emptyList()
                        accessToken = response.headers().get("Authorization") ?: ""

//                        responseToken?.token = refreshToken.firstOrNull()
                        Log.d("success", "login + $accessToken + $refreshToken")

                        // SharedPreferences에 accessToken 저장
                        saveAuthToken(responseToken?.token!!)

                        val intent = Intent(this@Login, TestHottopic::class.java)
                        intent.putExtra("token", accessToken)
                        startActivity(intent)
                        Toast.makeText(this@Login, "로그인 성공", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d("error", "login Response fail: ${response.errorBody()}")
                        Toast.makeText(this@Login, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("fail login", t.message.toString())
                    Toast.makeText(this@Login, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //서버 응답의 contentLength를 확인하고, contentLength가 0이면 null을 반환
    private val nullOnEmptyConverterFactory = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
        }
    }
}