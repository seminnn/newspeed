package com.newspeed.myapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

const val SHARED_PREF_NAME = "your_shared_pref_name"
const val USER_TOKEN_KEY = "user_token_key"

fun Context.saveAuthToken(token: String) {
    val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString(USER_TOKEN_KEY, token)
    }
}

fun Context.getAuthToken(): String {
    val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(USER_TOKEN_KEY, "") ?: ""
}