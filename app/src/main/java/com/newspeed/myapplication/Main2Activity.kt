package com.newspeed.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ActivityMain2Binding

class Main2Activity : AppCompatActivity() {
    private var mBinding: ActivityMain2Binding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent1 = Intent(this, Login::class.java)
        binding.loginbtn.setOnClickListener{startActivity(intent1) }

        val intent2 = Intent(this, Join::class.java)
        binding.joinbtn.setOnClickListener{startActivity(intent2) }
    }
}