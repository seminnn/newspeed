package com.newspeed.myapplication

import com.newspeed.myapplication.bookmark.BookmarkData
import com.newspeed.myapplication.cardnews.ItemData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // 로그인 API 호출
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // 회원가입 API 호출
    @POST("register")
    fun registerUser(@Body request: SignupRequest): Call<SignupResponse>

    //핫토픽 뉴스 기사 목록
    @GET("news/hot-topics")
    fun getHotTopics(
        @Query("category") category: String
    ): Call<List<HotTopicResponse>>

    //개인화 버블 목록
    @GET("news/recommendations")
    fun getBubbles(
        @Header("Authorization") token: String
    ): Call<List<BubbleResponse>>

    //뉴스 세 줄 요약
    @GET("/news/summary")
    fun getNewsDetails(@Query("cid") newsId: Int): Call<NewsDetailsResponse>

    //기사 조회
    @GET("/news/show")
    fun getNewsShow(
        @Header("Authorization") authHeader: String,
        @Query("nid") nid: Int): Call<NewsShowResponse>

    //북마크
    @POST("/bookmarks/add")
    fun addBookmark(
        @Header("Authorization") authHeader: String,
        @Body bookmarkRequest: BookmarkRequest): Call<BookmarkResponse>

    @GET("/bookmarks/inquiry")
    fun getBookmarks(@Header("Authorization") authorizationHeader: String): Call<List<BookmarkData>>

    @HTTP(method = "DELETE", path = "/bookmarks/remove", hasBody = true)
    fun deleteBookmark(
        @Header("Authorization") authorizationHeader: String,
        @Body request: BookmarkRequest): Call<BookmarkResponse>

    //카드뉴스
    @GET("news/cards")
    fun getNewsCards(): Call<List<ItemData>>

    //체류시간 계산
    @POST("/news/staytime")
    suspend fun sendNewsStayTime(@Body request: NewsStayTimeRequest): Response<Unit>
}

