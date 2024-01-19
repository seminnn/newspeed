package com.newspeed.myapplication.bookmark

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.newspeed.myapplication.ApiService
import com.newspeed.myapplication.ArticleActivity
import com.newspeed.myapplication.BookmarkRequest
import com.newspeed.myapplication.databinding.ActivityBookmarkBinding
import com.newspeed.myapplication.getAuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val dataSet = mutableListOf<BookmarkData>()
    private lateinit var apiService: ApiService // 클래스 레벨 변수로 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initApiService() // apiService 초기화
        loadBookmarksFromServer()
        initRecyclerView() // Assuming you want to load bookmarks when the activity is created
    }

    private fun initApiService() {
        val authToken = getAuthToken()
        val authorizationHeader = "Bearer $authToken"

        apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.mybookmarkview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BookmarkAdapter(dataSet, apiService, getAuthToken()) // apiService와 authToken을 전달
        recyclerView.adapter = adapter

        // Set item click listener for the adapter
        adapter.setOnItemClickListener { clickedItem ->
            handleItemClick(clickedItem)
        }
    }
    private fun loadBookmarksFromServer() {
        val authToken = getAuthToken()
        val authorizationHeader = "Bearer $authToken"

        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getBookmarks(authorizationHeader)
        call.enqueue(object : Callback<List<BookmarkData>> {
            override fun onResponse(call: Call<List<BookmarkData>>, response: Response<List<BookmarkData>>) {
                if (response.isSuccessful) {
                    val bookmarks = response.body() ?: emptyList()
                    dataSet.clear()
                    dataSet.addAll(bookmarks)
                    binding.mybookmarkview.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@BookmarkActivity, "Request failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<BookmarkData>>, t: Throwable) {
                Toast.makeText(this@BookmarkActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun handleItemClick(clickedItem: BookmarkData) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("nid", clickedItem.nid)
        startActivity(intent)
    }
    }
