package com.example.appdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appdiary.SQLite.SQLHelper
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var sqlHelper: SQLHelper
    lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent = intent
        id = intent.getStringExtra("detail_time").toString()
        detail_tvDay.text = intent.getStringExtra("detail_day")
        detail_tvTitle.text = intent.getStringExtra("detail_title")
        detail_tvContent.text = intent.getStringExtra("detail_content")

        detail_btnBack.setOnClickListener {
            startActivity(Intent(baseContext, AllDiaryActivity::class.java))
            finish()
        }

        detail_btnUpdate.setOnClickListener {
            var intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("detail_time",id)
            intent.putExtra("detail_day", detail_tvDay.text.toString())
            intent.putExtra("detail_title", detail_tvTitle.text.toString())
            intent.putExtra("detail_content", detail_tvContent.text.toString())
            startActivity(intent)
            finish()
        }

        detail_btnRemove.setOnClickListener {
            sqlHelper = SQLHelper(baseContext)
            val string = intent.getStringExtra("detail_time").toString()
            try {
                sqlHelper.deleteDiary(string)
                Toast.makeText(this, "Delete successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, AllDiaryActivity::class.java))
                finish()
            }catch (e:Exception){
                println("Delete error!")
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, AllDiaryActivity::class.java))
        finish()
    }
}