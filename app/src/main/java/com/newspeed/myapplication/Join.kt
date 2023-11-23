package com.newspeed.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ActivityJoinBinding

class Join : AppCompatActivity() {
    private var mBinding: ActivityJoinBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnnext.setOnClickListener {
            val id = binding.joinID.text.toString().trim()
            val pw = binding.joinPw.text.toString().trim()
            val name = binding.joinName.text.toString().trim()

            val intent = Intent(this, Join2::class.java)
            startActivity(intent)
    }
}}