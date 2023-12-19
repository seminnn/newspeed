package com.newspeed.myapplication

data class NewsDetailsResponse(
    val keyword: String,
    //세 줄 요약
    val s_state: String,
    //기사 목록
    val articles: List<Article>
)

data class Article(
    val title: String,
    val content: String,
    val nid: Int
)
