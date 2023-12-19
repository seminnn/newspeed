package com.newspeed.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.newspeed.myapplication.databinding.ArticlelayoutBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleActivity : AppCompatActivity() {
    private var mBinding: ArticlelayoutBinding? = null
    private val binding get() = mBinding!!
    private var activityStartTime: Long = 0L // 초기화


    //private val sdfServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    //private val sdfLocal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    var nid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ArticlelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달된 카테고리 정보를 가져옴(받아오는게 맞는지???)
        //val category = intent.getStringExtra("category")

        // 활동이 시작될 때의 시간 기록
        activityStartTime = System.currentTimeMillis()

        // 기사 조회 API 호출
        nid = intent.getIntExtra("nid", -1)
        if (nid != -1 ) {
            loadNews(nid)
        } else {
            Toast.makeText(this, "Invalid news item", Toast.LENGTH_SHORT).show()
        }
    }

    //뒤로가기 버튼 구현
    var backPressedTime: Long = 0
    override fun onBackPressed() {
        // 2.5초 이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            // 서버에 머문 시간을 전송
            val timeSpent = System.currentTimeMillis() - activityStartTime
            sendNewsStayTimeToServer(timeSpent)
            super.onBackPressed()
            return
        }
        //토큰 넣기?

        Toast.makeText(this, "한 번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    private fun loadNews(nid: Int) {
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.35.186:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getNewsShow(nid)
        call.enqueue(object : Callback<NewsShowResponse> {
            override fun onResponse(call: Call<NewsShowResponse>, response: Response<NewsShowResponse>) {
                if (response.isSuccessful) {
                    val newsItem = response.body()

                    if (newsItem != null) {

                        // 기사 정보를 화면에 표시
                        binding.articleTitle.text = newsItem.title
                        binding.articleContent.text = newsItem.content

                        // 이미지가 있는 경우에만 이미지를 불러옴
                        if (!newsItem.img_url.isNullOrEmpty()) {
                            Glide.with(this@ArticleActivity)
                                .load(newsItem.img_url)
                                .into(binding.articleImage)
                        }
                        // 기사 조회 후, 체류 시간을 서버로 전송
                        val timeSpent = System.currentTimeMillis() - activityStartTime
                        sendNewsStayTimeToServer(timeSpent)

                    } else {
                        Toast.makeText(this@ArticleActivity, "서버 응답이 null입니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ArticleActivity, "응답이 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsShowResponse>, t: Throwable) {
                Log.d("fail news", "Error during API call", t)
                Toast.makeText(this@ArticleActivity, "에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendNewsStayTimeToServer(timeSpent: Long) {
        // Retrofit을 사용하여 /news/staytime 엔드포인트에 시간 정보를 전송
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.35.186:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)


        val token = getAuthToken() // 사용자 토큰을 여기에 넣어주세요
        val cid = "your_cid_here" // 사용자가 어떤 뉴스를 보고 있었는지에 대한 정보를 넣어주세요

        // NewsStayTimeRequest 객체 생성
        val time: Int = (timeSpent / 100).toInt()
        val request = NewsStayTimeRequest(token, time, nid.toString())

        // Retrofit을 사용하여 /news/staytime 엔드포인트에 POST 요청
        lifecycleScope.launch {
            try {
                val response = apiService.sendNewsStayTime(request)

                if (response.isSuccessful) {
                    // 성공적으로 처리된 경우
                    // response.body()에서 필요한 데이터를 가져와 처리
                } else {
                    // 실패한 경우
                    // response.errorBody()에서 실패 이유를 가져와 처리
                }
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                e.printStackTrace()
            }
        }
    }
    private fun formatTimeForServer(timeMillis: Long): String {
        val sdfServer = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formattedTime = sdfServer.format(Date(timeMillis))
        return formattedTime
    }
}

