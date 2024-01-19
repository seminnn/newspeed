package com.newspeed.myapplication

import java.util.Date

data class NewsShowResponse(
    val title: String,
    val content: String,
    val img_url: String,
    val category: String,
    val j_info: String,
    val company: String,
    val cid: Int, // 추가된 필드
    val bookmark: Int // 추가된 필드
)