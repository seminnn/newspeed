package com.newspeed.myapplication.bubbleclick

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.newspeed.myapplication.ArticleActivity
import com.newspeed.myapplication.databinding.ActivityBubbleclickBinding

class BubbleClickActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBubbleclickBinding
    private val bubbledataSet = mutableListOf<BubbleNewsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBubbleclickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializelist()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.myrecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BubbleNewsAdapter(bubbledataSet)
        recyclerView.adapter = adapter

        // RecyclerView 아이템 클릭 리스너 설정
        adapter.setOnItemClickListener { clickedItem ->
            handleItemClick(clickedItem)
        }
    }

    private fun initializelist() {
        // 임의로 데이터 넣어서 만들어봄
        bubbledataSet.add(BubbleNewsData("버블뉴스 예시1", ""))
        bubbledataSet.add(BubbleNewsData("버블뉴스 예시2", ""))
        bubbledataSet.add(BubbleNewsData("버블뉴스 예시3", ""))
    }

    private fun handleItemClick(clickedItem: BubbleNewsData) {
        // 클릭한 아이템에 대한 처리
        // 예: 새로운 activity 시작
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra("selectedItem", clickedItem.title)
        startActivity(intent)
    }
}