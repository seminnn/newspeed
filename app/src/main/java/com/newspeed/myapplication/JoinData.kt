package com.newspeed.myapplication

data class SignupRequest(
    val email: String,
    val id: String,
    val passwd: String,
    val category: List<Int>
)

data class SignupResponse(
    val status: Int,
    val message: String?,
    val solution: String?,
    val token: String
)
