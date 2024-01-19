package com.newspeed.myapplication.joinlogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.databinding.ActivityJoin2Binding

class Join2 : AppCompatActivity() {
    private var mBinding: ActivityJoin2Binding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityJoin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, Login::class.java)
        binding.startbtn.setOnClickListener{startActivity(intent) }
    }
}