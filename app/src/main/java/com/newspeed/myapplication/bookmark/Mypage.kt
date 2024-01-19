package com.newspeed.myapplication.bookmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.newspeed.myapplication.TestBubble
import com.newspeed.myapplication.TestHottopic
import com.newspeed.myapplication.cardnews.NewsActivity
import com.newspeed.myapplication.databinding.ActivityMypageBinding
import com.newspeed.myapplication.joinlogin.MainActivity

class Mypage : AppCompatActivity() {
    private var mBinding: ActivityMypageBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 화면 전환 설정
        val intent5 = Intent(this, MainActivity::class.java)
        binding.goLogout.setOnClickListener {
            startActivity(intent5)
            Toast.makeText(applicationContext, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
        }
        val intent6 = Intent(this, BookmarkActivity::class.java)
        binding.goBookmark.setOnClickListener{startActivity(intent6) }

        val intent1 = Intent(this, TestHottopic::class.java)
        binding.hotbtn.setOnClickListener { startActivity(intent1) }

        val intent2 = Intent(this, NewsActivity::class.java)
        binding.cardbtn.setOnClickListener { startActivity(intent2) }

        val intent3 = Intent(this, Mypage::class.java)
        binding.mypagebtn.setOnClickListener { startActivity(intent3) }

        val intent4 = Intent(this, TestBubble::class.java)
        binding.bubblebtn.setOnClickListener { startActivity(intent4) }
    }
}