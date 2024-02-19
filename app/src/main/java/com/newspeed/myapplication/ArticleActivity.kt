package com.newspeed.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.newspeed.myapplication.databinding.ArticlelayoutBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ArticleActivity : AppCompatActivity() {
    private var mBinding: ArticlelayoutBinding? = null
    private val binding get() = mBinding!!
    private var activityStartTime: Long = 0L
    var nid = 0
    var cid = 0

    private var responseToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ArticlelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        responseToken = getAuthToken()

        activityStartTime = System.currentTimeMillis()

        nid = intent.getIntExtra("nid", -1)
        if (nid != -1) {
            loadNews(nid)
        } else {
            Toast.makeText(this, "잘못된 기사 ID", Toast.LENGTH_SHORT).show()
        }

        cid = intent.getIntExtra("cid", -1)

        binding.bookmarkbtn.setOnClickListener {
            addBookmarkToServer(nid)
        }
    }


    //뒤로가기 버튼 구현
    var backPressedTime: Long = 0
    override fun onBackPressed() {
        // 2.5초 이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            // 서버에 머문 시간을 전송
            super.onBackPressed()
            return
        }
        //토큰 넣기?
        Toast.makeText(this, "한 번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    //기사조회
    private fun loadNews(nid: Int) {

        val authToken = getAuthToken()
        val authorizationHeader = "Bearer $authToken"

        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val call = apiService.getNewsShow(authorizationHeader,nid)
        call.enqueue(object : Callback<NewsShowResponse> {
            override fun onResponse(call: Call<NewsShowResponse>, response: Response<NewsShowResponse>) {
                if (response.isSuccessful) {
                    val newsItem = response.body()

                    if (newsItem != null) {

                        // 기사 정보를 화면에 표시
                        binding.articleTitle.text = newsItem.title
                        binding.articleContent.text = newsItem.content
                        binding.articleCompany.text = newsItem.company
                        binding.articleName.text = newsItem.j_info

                        // 이미지가 있는 경우에만 이미지를 불러옴
                        if (!newsItem.img_url.isNullOrEmpty()) {
                            Glide.with(this@ArticleActivity)
                                .load(newsItem.img_url)
                                .into(binding.articleImage)
                        }
                    } else {
                        Toast.makeText(this@ArticleActivity, "서버 응답이 null입니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ArticleActivity, "응답이 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewsShowResponse>, t: Throwable) {
                Log.d("fail news", "Error during API call", t)
                Toast.makeText(this@ArticleActivity, "에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //북마크
    private fun addBookmarkToServer(nid: Int) {
        val authToken = getAuthToken()
        val authorizationHeader = "Bearer $authToken"

        val apiService = Retrofit.Builder()
            .baseUrl("http://192.168.0.14:5001") // 서버 URL로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val bookmarkRequest = BookmarkRequest(nid)
        val call = apiService.addBookmark(authorizationHeader, bookmarkRequest)
        call.enqueue(object : Callback<BookmarkResponse> {
            override fun onResponse(call: Call<BookmarkResponse>, response: Response<BookmarkResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ArticleActivity, "북마크가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ArticleActivity, "이미 추가된 북마크입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                Toast.makeText(this@ArticleActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

