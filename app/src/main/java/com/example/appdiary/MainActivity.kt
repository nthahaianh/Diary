package com.example.appdiary

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_password.*
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var startWeek = 1
    private var month = 0
    private var year = 0
    lateinit var sqlHelper: SQLHelper
    var calendar = Calendar.getInstance()
    private var page = 1
    private var titleList: MutableList<String> = mutableListOf()
    val adapter1 = ViewPagerAdapter(supportFragmentManager, lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sqlHelper = SQLHelper(baseContext)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        main_tvMonthYear?.text = "${month + 1}/$year"

        var sharedPreferences: SharedPreferences = this.getSharedPreferences("SharePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor?.putInt("spYear", year)
        editor?.putInt("spMonth", month)
        editor?.putInt("spDay", calendar.get(Calendar.DATE))
        editor?.apply()

        main_setting.setOnClickListener {
            var popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { it ->
                when(it.itemId){
                    R.id.menu_all_diary -> {
                        startActivity(Intent(baseContext,AllDiaryActivity::class.java))
                        true
                    }
                    R.id.menu_create_password -> {
                        displayDialog()
                        true
                    }
                    R.id.menu_remove_password -> {
                        editor.putBoolean("havePassword",false)
                        editor.apply()
                        Toast.makeText(this,"Done!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_backup->{
                        backup(sqlHelper.getAll())
                    }
                    R.id.menu_restore->{
                        restore()
                    }
                }
                false
            }
            popupMenu.show()
        }

        main_add.setOnClickListener {
            startActivity(Intent(baseContext,CreateActivity::class.java)) }

        var listFragment: MutableList<Fragment1> = mutableListOf()
        listFragment.add(Fragment1().newInstance(startWeek, month, year))
        listFragment.add(Fragment1().newInstance(startWeek, month, year))
        listFragment.add(Fragment1().newInstance(startWeek, month, year))

        adapter1.setLists(listFragment)
        adapter1.update(startWeek, month, year)
        viewpager_demo.apply {
            adapter = adapter1
            currentItem = 1
        }
        viewpager_demo.offscreenPageLimit = 2
        viewpager_demo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                page = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (page < 1) {
                        calendar.add(Calendar.MONTH, -1)
                    } else if (page > 1) {
                        calendar.add(Calendar.MONTH, 1)
                    }
                    month = calendar.get(Calendar.MONTH)
                    year = calendar.get(Calendar.YEAR)
                    main_tvMonthYear?.text = "${month + 1}/$year"
                    adapter1.update(startWeek, month, year)
                    viewpager_demo.setCurrentItem(1, false)
                }
            }

        })

        addData()
        setTitleView()
    }
    private fun restore() {
        var path = filesDir
        var fileReader= FileReader("$path/data.csv")
        var lines:List<String> =  fileReader.readLines()
        try {
            sqlHelper.deleteTable()
            lines.forEach {
                var items : List<String> = it.split(",")
                var myDate = MyDate(items[1].toInt(), items[2].toInt(), items[3].toInt())
                var newDiary = MyDiary(items[0], myDate, items[4], items[5])
                sqlHelper.insertUser(newDiary)
            }
            Toast.makeText(this,"Restore successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this,"Restore error!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun backup(diaryList: MutableList<MyDiary>) {
        var fileWriter: FileWriter? = null
        var path = filesDir
        try {
            fileWriter = FileWriter("$path/data.csv")

            for (diary in diaryList) {
                fileWriter.append(diary.time)
                fileWriter.append(',')
                fileWriter.append(diary.myDate.year.toString())
                fileWriter.append(',')
                fileWriter.append(diary.myDate.month.toString())
                fileWriter.append(',')
                fileWriter.append(diary.myDate.day.toString())
                fileWriter.append(',')
                fileWriter.append(diary.title)
                fileWriter.append(',')
                fileWriter.append(diary.content)
                fileWriter.append('\n')
            }
            Toast.makeText(this,"Backup successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter?.flush()
                fileWriter?.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }

    private fun setTitleView() {
        var titleList2: MutableList<String> = mutableListOf()
        for (i in 0..6) {
            titleList2.add(titleList[i + startWeek])
        }
        val dayInWeekAdapter = DayInWeekAdapter(titleList2)
        val titleLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(
            applicationContext,
            7
        )
        rv_title.layoutManager = titleLayoutManager
        rv_title.adapter = dayInWeekAdapter
    }

    fun changeStartWeek(view: View) {
        val strings = arrayOf("SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT")
        var index = 0
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Select the day start the week")
            .setSingleChoiceItems(
                strings, 0
            ) { _, which -> index = which }
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                startWeek = index + 1
                setTitleView()
                adapter1.update(startWeek, month, year)
            }
            .setNegativeButton(
                "CANCEL"
            ) { _, _ ->
            }
            .create()
        alertDialog.show()
    }

    private fun addData() {
        titleList.add("SAT")
        titleList.add("SUN")
        titleList.add("MON")
        titleList.add("TUE")
        titleList.add("WED")
        titleList.add("THUR")
        titleList.add("FRI")
        titleList.add("SAT")
        titleList.add("SUN")
        titleList.add("MON")
        titleList.add("TUE")
        titleList.add("WED")
        titleList.add("THUR")
        titleList.add("FRI")
    }

    private fun displayDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_password)

        val window = dialog.window ?: return
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes

        dialog.dialog_btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.dialog_btnConfirm.setOnClickListener {
            var sharedPreferences: SharedPreferences = this.getSharedPreferences("SharePreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            if (dialog.dialog_etInput.text.toString().isNullOrBlank()){
                Toast.makeText(baseContext, "Invalid: Password is null or blank!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(baseContext, "Password is '${dialog.dialog_etInput.text}'", Toast.LENGTH_LONG).show()
                editor.putString("spPassword",dialog.dialog_etInput.text.toString())
                editor.putBoolean("havePassword",true)
                editor.apply()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}