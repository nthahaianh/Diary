package com.example.appdiary

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appdiary.Model.MyDate
import com.example.appdiary.Model.MyDiary
import com.example.appdiary.SQLite.SQLHelper
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_update.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {
    lateinit var sqlHelper: SQLHelper
    var day = 1
    var month = 0
    var year = 2000
    var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        sqlHelper = SQLHelper(baseContext)

        val intent = intent
        id = intent.getStringExtra("detail_time").toString()
        update_tvDate.text = intent.getStringExtra("detail_day")
        update_etTitle.setText(intent.getStringExtra("detail_title"))
        update_etContent.setText(intent.getStringExtra("detail_content"))

        var itemDate : List<String> = (intent.getStringExtra("detail_day").toString()).split("/")
        year = itemDate[0].toInt()
        month = itemDate[1].toInt() - 1
        day = itemDate[2].toInt()

        update_ivClose.setOnClickListener { finish() }
        update_btnSave.setOnClickListener {
            val myDate = MyDate(year, month, day)
            val newDiary = MyDiary(
                id,
                myDate,
                update_etTitle.text.toString(),
                update_etContent.text.toString()
            )
            try {
                sqlHelper.updateDiary(newDiary)
                startActivity(Intent(baseContext, AllDiaryActivity::class.java))
                finish()
            }catch (e:Exception){
                println("Update error!")
                e.printStackTrace()
            }
        }
        update_tvDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    day = dayOfMonth
                    this.month = month
                    this.year = year
                    update_tvDate.text = day.toString() + "/" + (this.month + 1) + "/" + this.year
                }, year, month, day
            )
            datePickerDialog.show()
        }
    }
}