package com.newspeed.myapplication.bubbleclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.ArticleActivity
import com.newspeed.myapplication.NewsDetailsResponse
import com.newspeed.myapplication.NewsStayTimeRequest
import com.newspeed.myapplication.databinding.ActivityBubbleclickBinding
import com.newspeed.myapplication.getAuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BubbleClickActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBubbleclickBinding
    private val bubbledataSet = mutableListOf<BubbleNewsData>()

    // 토큰을 저장할 변수
    private var responseToken: String = ""
    private var activityStartTime: Long = 0L
    private var cid: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBubbleclickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 토큰 받아오기
        responseToken = getAuthToken()

        // 요약 조회 API 호출
        val cidString = intent.getStringExtra("cid")
        if (cidString != null ) {
            cid = cidString.toInt() // Assign value to cid
            loadNewsDetails(cid)
        } else {
            Toast.makeText(this, "Invalid news item", Toast.LENGTH_SHORT).show()
        }

        // RecyclerView 초기화
        initRecyclerView()

        // 활동 시작 시간 기록
        activityStartTime = System.currentTimeMillis()
        // Activity가 실행될 때 바로 sendNewsStayTimeToServer 호출
//        sendNewsStayTimeToServer(0L, cid.toString())
    }

    //뒤로가기 버튼 구현
    var backPressedTime: Long = 0

    override fun onBackPressed() {
        // 2.5초 이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            val timeSpent = System.currentTimeMillis() - activityStartTime
            sendNewsStayTimeToServer(timeSpent, cid.toString())
            super.onBackPressed()
            return
        }
        //토큰 넣기?

        Toast.makeText(this, "한 번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    //
    private fun loadNewsDetails(cid:Int) {
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getNewsDetails(cid)
        call.enqueue(object : Callback<NewsDetailsResponse> {
            override fun onResponse(
                call: Call<NewsDetailsResponse>,
                response: Response<NewsDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    val newsDetails = response.body()
                    if (newsDetails != null) {
                        binding.newstitle.text = newsDetails.keyword
                        //세줄요약
                        binding.summarize.text = newsDetails.s_state
                        // articles을 BubbleNewsData로 변환
                        //val bubbleNewsDataList = newsDetails.articles.map { BubbleNewsData(it.title, it.content ?: "", it.nid) }
                        val bubbleNewsDataList = newsDetails.articles.map {
                            BubbleNewsData(it.title, it.content ?: "", it.nid)
                        }
                        sendNewsStayTimeToServer(System.currentTimeMillis() - activityStartTime, cid.toString())
                        // RecyclerView 설정
                        val adapter = BubbleNewsAdapter(bubbleNewsDataList)
                        binding.myrecyclerview.adapter = adapter
                        // 아이템 클릭 리스너 설정
                        adapter.setOnItemClickListener { clickedItem ->
                            handleItemClick(clickedItem, cid)
                        }
                    } else {
                        Toast.makeText(applicationContext, "서버 응답이 null입니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Bubble Response fail", "Error: ${response.message()}")
                    Toast.makeText(applicationContext, "응답이 실패했습니다", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<NewsDetailsResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun sendNewsStayTimeToServer(timeSpent: Long, cid: String) {
        Log.d("////cid", cid)
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val time: Int = (timeSpent / 100).toInt()
                val response = apiService.sendNewsStayTime(
                    NewsStayTimeRequest(responseToken, time, cid)
                )
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "체류시간 계산 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "응답 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.myrecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BubbleNewsAdapter(bubbledataSet)
        recyclerView.adapter = adapter
    }
    private fun handleItemClick(clickedItem: BubbleNewsData, cid: Int) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("token",responseToken) //토큰 전달
        intent.putExtra("nid", clickedItem.nid) // nid 값을 전달
        intent.putExtra("cid", cid) // cid 값을 전달

        startActivity(intent)
    }

}