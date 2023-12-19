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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookmarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookmarkBinding
    private val dataSet = mutableListOf<BookmarkData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView() // Assuming you want to load bookmarks when the activity is created
    }

    private fun initRecyclerView() {
        val recyclerView = binding.mybookmarkview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BookmarkAdapter(dataSet)
        recyclerView.adapter = adapter

        // Set item click listener for the adapter
        adapter.setOnItemClickListener { clickedItem ->
            handleItemClick(clickedItem)
        }
    }



    private fun handleItemClick(clickedItem: BookmarkData) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("selectedItem", clickedItem.title)
        startActivity(intent)
    }


    }
