package com.newspeed.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.newspeed.myapplication.bookmark.Mypage
import com.newspeed.myapplication.bubbleclick.BubbleClickActivity
import com.newspeed.myapplication.cardnews.NewsActivity
import com.newspeed.myapplication.databinding.FragmentHottopicBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestHottopic : AppCompatActivity() {

    private lateinit var binding: FragmentHottopicBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BubbleAdapter
    private lateinit var layoutManager: SpannedGridLayoutManager
    // LiveData 선언
    private val hotTopicsLiveData: MutableLiveData<List<HotTopicResponse>> = MutableLiveData()
    // 토큰 배포
    private var responseToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHottopicBinding.inflate(layoutInflater)
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

        // 토큰
        responseToken = intent.getStringExtra("token").toString()
        Log.d("////token", responseToken)

        // recyclerview
        recyclerView = binding.hotView

        // Observer 등록
        hotTopicsLiveData.observe(this, Observer { hotTopics ->
            hotTopics?.let {
                val newData = BubbleItem.createFromHotTopics(hotTopics.toMutableList())
                adapter = BubbleAdapter(this, newData) { selectedItem ->
                    handleItemClick(selectedItem)
                }
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(0)
            }
        })

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


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        recyclerView.startAnimation(animation)

        setCheckBoxListeners()
        // 데이터 갱신
    }

    private fun updateRecyclerViewData(category: String, ) {
        loadHotTopics(category)
    }

    private fun handleItemClick(selectedItem: BubbleItem) {
        // 클릭한 아이템에 대한 처리
        val intent = Intent(this, BubbleClickActivity::class.java)
        //토큰 전달
        intent.putExtra("token", responseToken)
        intent.putExtra("cid", selectedItem.cid)
        startActivity(intent)
    }

    fun getToken(): String? {
        return responseToken
    }

    //기사 목록을 버블에 띄우기
    private fun loadHotTopics(category: String) {
        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001")  // 실제 서버 주소로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getHotTopics(category, responseToken)

        call.enqueue(object : Callback<List<HotTopicResponse>> {
            override fun onResponse(
                call: Call<List<HotTopicResponse>>,
                response: Response<List<HotTopicResponse>>
            ) {
                if (response.isSuccessful) {
                    val hotTopics = response.body()
                    hotTopicsLiveData.value= hotTopics
                    if (hotTopics != null) {
                        // sumCom 기준으로 내림차순 정렬
                        val sortedHotTopics = hotTopics.sortedByDescending { it.sumCom }
                        // hotTopicsLiveData에 정렬된 데이터를 업데이트
                        hotTopicsLiveData.value = sortedHotTopics
                        // hotTopics를 BubbleItem으로 변환하여 RecyclerView에 적용
                        val sortedBubbles = hotTopics.sortedByDescending { it.sumCom }
                        // 방금 전에 알려준 코드 추가
                        val orderOfIndices = arrayOf(5, 1, 0, 8, 9, 7, 2, 3, 6, 4)
                        val newData = orderOfIndices
                            .filter { it < sortedBubbles.size } // 범위를 벗어나지 않도록 필터링
                            .map { index ->
                                BubbleItem(
                                    keyword = sortedBubbles[index].keyword,
                                    sum_com = sortedBubbles[index].sumCom,
                                    cid = sortedBubbles[index].cid
                                )
                            }
                        adapter.updateData(newData)
                        adapter.notifyDataSetChanged()
                        recyclerView.requestLayout() }

                    else {
                        // 응답이 null인 경우에 대한 처리
                        Toast.makeText(applicationContext, "서버 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 실패 처리
                    Toast.makeText(applicationContext, "뉴스 추천 가져오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<HotTopicResponse>>, t: Throwable) {
                Log.e("API 오류", "API 호출 중 오류 발생: ${t.message}", t)
                Toast.makeText(applicationContext, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    //체크박스
    private fun setCheckBoxListeners() {
        val checkBoxMap = mapOf(
            R.id.politic_btn to "politics",
            R.id.economy_btn to "economy",
            R.id.society_btn to "society",
            R.id.life_btn to "culture",
            R.id.IT_btn to "science",
            R.id.world_btn to "world"
        )
        for (checkBoxId in checkBoxMap.keys) {
            findViewById<CheckBox>(checkBoxId).setOnClickListener {
                // 클릭한 체크박스에 연결된 카테고리 가져오기
                val category = checkBoxMap[checkBoxId]
                if (category != null) {
                    // 카테고리를 핫토픽 불러오는 서버로 보내기
                    loadHotTopics(category)
                }
            }
        }
    }

}
