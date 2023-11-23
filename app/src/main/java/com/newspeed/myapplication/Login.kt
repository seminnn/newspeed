package com.newspeed.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ActivityLoginBinding

import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

class Login : AppCompatActivity() {
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 클릭 이벤트
        binding.btnlogin.setOnClickListener {
            val id = binding.loginID.text.toString().trim()
            val pw = binding.loginPw.text.toString().trim()
            val name = binding.loginName.text.toString().trim()
            Thread {
                try {
                    val url = URL("http://127.0.0.1:5001//login") // Flask 서버 URL
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    httpURLConnection.requestMethod = "POST"
                    httpURLConnection.doOutput = true
                    httpURLConnection.setRequestProperty("Content-Type", "application/json")

                    val jsonInput = JSONObject()
                    jsonInput.put("id", id)
                    jsonInput.put("password", pw)
                    jsonInput.put("name", name)

                    httpURLConnection.outputStream.use { os ->
                        val input = jsonInput.toString().toByteArray(Charsets.UTF_8)
                        os.write(input, 0, input.size)
                    }

                    val responseCode = httpURLConnection.responseCode
                    runOnUiThread {
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // 성공 처리
                            val intent = Intent(this@Login, TestBubble::class.java)
                            startActivity(intent)
                        } else {
                            // 실패 처리
                            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(applicationContext, "오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}

