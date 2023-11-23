package com.newspeed.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.newspeed.myapplication.bubbleclick.BubbleClickActivity
import com.newspeed.myapplication.cardnews.NewsActivity
import com.newspeed.myapplication.databinding.FragmentBubbleBinding


class TestBubble : AppCompatActivity() {

    private lateinit var binding: FragmentBubbleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BubbleAdapter
    private lateinit var layoutManager: SpannedGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBubbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent1 = Intent(this, TestHottopic::class.java)
        binding.hotbtn.setOnClickListener { startActivity(intent1) }

        val intent2 = Intent(this, NewsActivity::class.java)
        binding.cardbtn.setOnClickListener { startActivity(intent2) }

        // recyclerview
        recyclerView = binding.bubbleView

        adapter = BubbleAdapter(this, BubbleItem.createSampleData()) { selectedItem ->
            // 클릭 이벤트 처리
            // selectedItem을 사용하여 클릭한 아이템에 대한 작업을 수행
            handleItemClick(selectedItem)
        }

        layoutManager = SpannedGridLayoutManager(
            orientation = SpannedGridLayoutManager.Orientation.HORIZONTAL,
            spans = 7
        )
        layoutManager.itemOrderIsStable = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        layoutManager.itemOrderIsStable = true

        // Set your SpanSizeLookup here
        layoutManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
            when (position % 11) {
                9, 1, 7, 10, 6 ->
                    SpanSize(2, 2)

                2 ->
                    SpanSize(3, 3)

                else ->
                    SpanSize(1, 1)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        recyclerView.startAnimation(animation)

        // 데이터 갱신
        updateRecyclerViewData()
    }

    private fun updateRecyclerViewData() {
        // 데이터를 업데이트한 후 RecyclerView에 적용
        val newData = BubbleItem.createSampleData()
        adapter.updateData(newData)

        // 데이터 변경을 알림
        adapter.notifyDataSetChanged()

        // 레이아웃 갱신
        recyclerView.requestLayout()
    }


    private fun handleItemClick(selectedItem: String) {
        // 클릭한 아이템에 대한 처리
        val intent = Intent(this, BubbleClickActivity::class.java)
        intent.putExtra("selectedItem", selectedItem)
        startActivity(intent)
    }
}

