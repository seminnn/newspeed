package com.newspeed.myapplication.cardnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.newspeed.myapplication.TestBubble
import com.newspeed.myapplication.TestHottopic
import com.newspeed.myapplication.ArticleActivity
import com.newspeed.myapplication.BookMark
import com.newspeed.myapplication.MainActivity
import com.newspeed.myapplication.databinding.FragmentNewsBinding


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: FragmentNewsBinding
    val dataSet = mutableListOf<ItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializelist()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        val recyclerView = binding.myrecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MyAdapter(dataSet)
        recyclerView.adapter = adapter

        // Set item click listener for the adapter
        adapter.setOnItemClickListener { clickedItem ->
            handleItemClick(clickedItem)
        }
    }

    fun initializelist() { //임의로 데이터 넣어서 만들어봄
            dataSet.add(ItemData("카드뉴스 예시1",""))
            dataSet.add(ItemData("카드뉴스 예시2",""))
            dataSet.add(ItemData("카드뉴스 예시3",""))


            val intent = Intent(this, TestHottopic::class.java)
            binding.hotbtn.setOnClickListener {
                startActivity(intent)
            }
            val intent2 = Intent(this, TestBubble::class.java)
            binding.bubblebtn.setOnClickListener {
                startActivity(intent2)
            }
        val intent3 = Intent(this, MainActivity::class.java)
        binding.logout.setOnClickListener {
            startActivity(intent3)
            Toast.makeText(applicationContext, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
        }
        val intent4 = Intent(this, BookMark::class.java)
        binding.bookmark.setOnClickListener {
            startActivity(intent4)
        }

        }
    private fun handleItemClick(clickedItem: ItemData) {
        // 클릭한 아이템에 대한 처리
        // 예: 새로운 activity 시작
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("selectedItem", clickedItem.title)
        startActivity(intent)
    }
    }


