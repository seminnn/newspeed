package com.newspeed.myapplication.cardnews

import java.util.Date

data class ItemData(
    val title: String,
    val content: String,
    val nid: Int,
    val category: String,
    var ymd: Date?
)
