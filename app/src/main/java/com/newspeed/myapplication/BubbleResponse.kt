package com.newspeed.myapplication

import com.google.gson.annotations.SerializedName

data class BubbleResponse(
    @SerializedName("keyword") val keyword: String,
    @SerializedName("sum_com") val sumCom: Int,
    @SerializedName("cid") val cid: String
)