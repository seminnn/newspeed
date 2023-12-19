package com.newspeed.myapplication
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest(
    val id: String,
    val passwd: String
)

data class LoginResponse(
    var token: String?

)