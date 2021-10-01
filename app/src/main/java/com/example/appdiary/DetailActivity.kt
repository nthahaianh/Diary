package com.example.appdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent = intent
        detail_tvDay.text = intent.getStringExtra("detail_day")
        detail_tvTitle.text = intent.getStringExtra("detail_title")
        detail_tvContent.text = intent.getStringExtra("detail_content")

        detail_btnBack.setOnClickListener {
            finish()
        }
    }
}