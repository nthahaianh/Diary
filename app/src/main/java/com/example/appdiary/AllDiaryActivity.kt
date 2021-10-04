package com.example.appdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appdiary.Adapter.DiaryAdapter
import com.example.appdiary.Model.MyDiary
import com.example.appdiary.SQLite.SQLHelper
import kotlinx.android.synthetic.main.activity_all_diary.*

class AllDiaryActivity : AppCompatActivity() {
    lateinit var sqlHelper: SQLHelper
    var diaryList: MutableList<MyDiary> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_diary)
        sqlHelper = SQLHelper(baseContext)
        diaryList = sqlHelper.getAll()
        diaryList.sortBy { it.myDate.toString() }
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        var adapter = DiaryAdapter(diaryList)
        all_diary_rv.adapter = adapter
        all_diary_rv.layoutManager = layoutManager
        adapter.setCallBack {
            var intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("detail_time", diaryList[it].time)
            intent.putExtra("detail_day", diaryList[it].myDate.toString())
            intent.putExtra("detail_title", diaryList[it].title)
            intent.putExtra("detail_content", diaryList[it].content)
            startActivity(intent)
            finish()
        }
        btnBack.setOnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }
        btnShow.setOnClickListener {
            if (btnShow.isChecked) {
                var str = etInput.text.toString()
                var findList: MutableList<MyDiary> = mutableListOf()
                for (item in diaryList) {
                    if (item.content.contains(str)) {
                        findList.add(item)
                    }
                }
                if (findList.size > 0) {
                    var newAdapter = DiaryAdapter(findList)
                    all_diary_rv.adapter = newAdapter
                    all_diary_rv.layoutManager = layoutManager
                    newAdapter.setCallBack {
                        var intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("detail_day", findList[it].myDate.toString())
                        intent.putExtra("detail_title", findList[it].title)
                        intent.putExtra("detail_content", findList[it].content)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(baseContext, "Don't have any result", Toast.LENGTH_SHORT).show()
                }
            } else {
                all_diary_rv.adapter = adapter
                adapter.setCallBack {
                    var intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("detail_day", diaryList[it].myDate.toString())
                    intent.putExtra("detail_title", diaryList[it].title)
                    intent.putExtra("detail_content", diaryList[it].content)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext, MainActivity::class.java))
        finish()
    }
}