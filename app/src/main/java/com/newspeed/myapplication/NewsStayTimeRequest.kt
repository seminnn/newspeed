package com.newspeed.myapplication

import com.google.gson.annotations.SerializedName

data class NewsStayTimeRequest(
    @SerializedName("token") val token: String,
    @SerializedName("time") val time: Int,
    @SerializedName("cid") val cid: String
)