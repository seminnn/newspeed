package com.newspeed.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ActivityBookmarkBinding

class BookMark : AppCompatActivity() {
    private var mBinding: ActivityBookmarkBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)}}