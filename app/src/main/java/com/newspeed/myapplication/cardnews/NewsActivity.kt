package com.newspeed.myapplication.cardnews

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.ArticleActivity
import com.newspeed.myapplication.BookmarkRequest
import com.newspeed.myapplication.R
import com.newspeed.myapplication.TestBubble
import com.newspeed.myapplication.TestHottopic
import com.newspeed.myapplication.bookmark.BookmarkActivity
import com.newspeed.myapplication.bookmark.Mypage
import com.newspeed.myapplication.databinding.FragmentNewsBinding
import com.newspeed.myapplication.joinlogin.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: FragmentNewsBinding
    private val dataSet = mutableListOf<ItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //화면전환
        val intent1 = Intent(this, TestHottopic::class.java)
        binding.hotbtn.setOnClickListener { startActivity(intent1) }

        val intent2 = Intent(this, NewsActivity::class.java)
        binding.cardbtn.setOnClickListener { startActivity(intent2) }

        val intent3 = Intent(this, Mypage::class.java)
        binding.mypagebtn.setOnClickListener { startActivity(intent3) }

        val intent4 = Intent(this, TestBubble::class.java)
        binding.bubblebtn.setOnClickListener { startActivity(intent4) }

        // 데이터 초기화 및 RecyclerView 설정
        initRecyclerView()

        // 카드뉴스 데이터 로드
        loadNewsCards()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.myrecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 설정
        val adapter = MyAdapter(dataSet)
        recyclerView.adapter = adapter


    }

    private fun loadNewsCards() {
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001") // 실제 서버 주소로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getNewsCards()
        call.enqueue(object : Callback<List<ItemData>> {
            override fun onResponse(call: Call<List<ItemData>>, response: Response<List<ItemData>>) {
                if (response.isSuccessful) {
                    // 서버 응답이 성공적인 경우
                    val newsList = response.body()
                    if (newsList != null) {
                        // RecyclerView에 데이터 설정
                        val adapter = MyAdapter(newsList)
                        binding.myrecyclerview.adapter = adapter

                        // 아이템 클릭 리스너 설정
                        adapter.setOnItemClickListener { clickedItem ->
                            handleItemClick(clickedItem)
                        }

                    } else {
                        // 서버 응답이 null인 경우
                        Toast.makeText(applicationContext, "서버 응답이 null입니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 서버 응답이 실패한 경우
                    Toast.makeText(applicationContext, "응답이 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemData>>, t: Throwable) {
                // 통신 실패 시
                Toast.makeText(applicationContext, "에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleItemClick(clickedItem: ItemData) {
        // 아이템 클릭 처리 로직
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("nid", clickedItem.nid)
        startActivity(intent)
    }

}