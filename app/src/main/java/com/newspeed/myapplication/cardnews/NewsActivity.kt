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

        // 화면 전환 설정
        val intent = Intent(this, MainActivity::class.java)
        binding.logout.setOnClickListener {
            startActivity(intent)
            Toast.makeText(applicationContext, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
        }
        val intent2 = Intent(this, BookmarkActivity::class.java)
        binding.bookmark.setOnClickListener {
            startActivity(intent2) }

        val intent3 = Intent(this, TestBubble::class.java)
        binding.bubblebtn.setOnClickListener { startActivity(intent3) }

        val intent4 = Intent(this, TestHottopic::class.java)
        binding.hotbtn.setOnClickListener { startActivity(intent4) }

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
            .baseUrl("http://192.168.0.12:5001") // 실제 서버 주소로 변경
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

                        // 북마크 클릭 리스너 설정
                        setBookmarkClickListener(newsList)
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

    private fun setBookmarkClickListener(newsList: List<ItemData>) {
        // 북마크 클릭 리스너 설정
        for (i in 0 until newsList.size) {
            val checkBoxId = resources.getIdentifier("bookmark_btn_$i", "id", packageName)
            val checkBox = findViewById<CheckBox>(checkBoxId)


        }
    }


    private fun handleItemClick(clickedItem: ItemData) {
        // 아이템 클릭 처리 로직
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("nid", clickedItem.nid)
        startActivity(intent)
    }

    private fun handleBookmarkClick(itemData: ItemData, checkBox: CheckBox) {
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.12:5001") // 실제 서버 주소로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val bookmarkRequest = BookmarkRequest(id = 1, nid = itemData.nid)

        if (checkBox.isChecked) {
            // 북마크가 체크되어 있으면 북마크 저장
            val call = apiService.setBookmark(bookmarkRequest)
            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@NewsActivity, "북마크가 저장되었습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@NewsActivity, "북마크 저장에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@NewsActivity, "에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}