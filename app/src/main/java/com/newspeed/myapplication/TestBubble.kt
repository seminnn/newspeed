package com.newspeed.myapplication
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.newspeed.myapplication.bubbleclick.BubbleClickActivity
import com.newspeed.myapplication.cardnews.NewsActivity
import com.newspeed.myapplication.databinding.FragmentBubbleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestBubble : AppCompatActivity() {

    private lateinit var binding: FragmentBubbleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BubbleAdapter
    private lateinit var layoutManager: SpannedGridLayoutManager


    private var responseToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBubbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 토큰 저장 예시
//        val receivedToken = "your_received_token"
//        saveAuthToken(receivedToken)

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
        responseToken = getAuthToken()
        loadBubbles() // 개인화 뉴스 추천 API 호출
    }

    private fun loadBubbles() {
        val apiService = createApiService()
        val authToken = getAuthToken()
        Log.d("////Token", authToken)

        val authorizationHeader = "Bearer $authToken"
        Log.d("AuthorizationHeader", authorizationHeader)
        val call = apiService.getBubbles(authorizationHeader)

        call.enqueue(object : Callback<List<BubbleResponse>> {
            override fun onResponse(
                call: Call<List<BubbleResponse>>,
                response: Response<List<BubbleResponse>>
            ) {
                if (response.isSuccessful) {
                    val bubbles = response.body()
                    if (bubbles != null) {
                        val newData = BubbleItem.createFromBubbles(bubbles)
                        adapter.updateData(newData)
                        adapter.notifyDataSetChanged()
                        recyclerView.requestLayout()
                    } else {
                        Toast.makeText(applicationContext, "서버 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        applicationContext,
                        "뉴스 추천 가져오기 실패: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<BubbleResponse>>, t: Throwable) {
                Toast.makeText(applicationContext, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleItemClick(selectedItem: BubbleItem) {
        // 클릭한 아이템에 대한 처리
        val intent = Intent(this, BubbleClickActivity::class.java)
        //토큰 전달
        intent.putExtra("token", responseToken)
        intent.putExtra("cid", selectedItem.cid)
        startActivity(intent)
    }

    private fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.35.186:5001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
//    private fun saveAuthToken(token: String) {
//        val sharedPreferences = getSharedPreferences("your_shared_pref_name", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("user_token_key", token)
//        editor.apply()
//    }
//
//    private fun getAuthToken(): String {
//        val sharedPreferences = getSharedPreferences("your_shared_pref_name", Context.MODE_PRIVATE)
//        return sharedPreferences.getString("user_token_key", "") ?: ""
//    }
    }