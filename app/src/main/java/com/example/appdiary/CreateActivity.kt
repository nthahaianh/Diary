package com.example.appdiary

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {
    lateinit var sqlHelper:SQLHelper
    var day = 1
    var month = 0
    var year = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        var sharedPreferences: SharedPreferences = baseContext!!.getSharedPreferences(
            "SharePreferences",
            Context.MODE_PRIVATE
        )
        day = sharedPreferences.getInt("spDay", 1)
        month = sharedPreferences.getInt("spMonth", 0)
        year = sharedPreferences.getInt("spYear", 2000)
        create_tvDate.text = "$day/${month+1}/$year"

        sqlHelper = SQLHelper(baseContext)

        create_ivClose.setOnClickListener { finish() }
        create_btnSave.setOnClickListener {
            var df = SimpleDateFormat("yyyyMMddHHmmss")
            val time: String = df.format(Calendar.getInstance().time)
            val myDate = MyDate(year,month,day)
            val newDiary=MyDiary(time,myDate,create_etTitle.text.toString(),create_etContent.text.toString())
            sqlHelper.insertUser(newDiary)
            finishAffinity()
            startActivity(Intent(this,MainActivity::class.java))
        }

        create_tvDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    day = dayOfMonth
                    this.month = month
                    this.year = year
                    create_tvDate.text = day.toString() + "/" + (this.month + 1) + "/" + this.year
                }, year, month, day
            )
            datePickerDialog.show()
        }
    }
}