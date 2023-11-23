package com.newspeed.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ArticlelayoutBinding

class ArticleActivity : AppCompatActivity() {
    private var mBinding: ArticlelayoutBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ArticlelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)}}