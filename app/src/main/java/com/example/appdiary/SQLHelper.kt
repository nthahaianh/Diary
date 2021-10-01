package com.example.appdiary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val queryCreateTable = "CREATE TABLE Diary (" +
                "id string not null primary key," +
                "year int," +
                "month int," +
                "day int," +
                "title string," +
                "content string" +
                ")"
        db.execSQL(queryCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF exists $DB_TABLE_DIARY")
            onCreate(db)
        }
    }

    fun insertUser(diary: MyDiary) {
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SQLText.T_DIARY_ID, diary.time)
        contentValues.put(SQLText.T_DIARY_YEAR, diary.myDate.year)
        contentValues.put(SQLText.T_DIARY_MONTH, diary.myDate.month)
        contentValues.put(SQLText.T_DIARY_DAY, diary.myDate.day)
        contentValues.put(SQLText.T_DIARY_TITLE, diary.title)
        contentValues.put(SQLText.T_DIARY_CONTENT, diary.content)
        sqLiteDatabase.insert(DB_TABLE_DIARY, null, contentValues)
    }

    fun getAll(): MutableList<MyDiary>{
        val diaryList: MutableList<MyDiary> = mutableListOf()
        val sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.query(
            false,
            DB_TABLE_DIARY,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_ID))
            val year = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_YEAR))
            val month = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_MONTH))
            val day = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_DAY))
            val date= MyDate(year, month, day)
            val title = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_TITLE))
            val content = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_CONTENT))
            val diary = MyDiary(id, date, title, content)
            diaryList.add(diary)
        }
        return diaryList
    }
    fun getDiaryByStr(string: String): MutableList<MyDiary>{
        val diaryList: MutableList<MyDiary> = mutableListOf()
        val sqLiteDatabase = readableDatabase
        val sql =
        "SELECT * FROM " + DB_TABLE_DIARY + " WHERE ${SQLText.T_DIARY_CONTENT} LIKE"

        val cursor = sqLiteDatabase.rawQuery(sql, arrayOf(string))
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_ID))
            val year = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_YEAR))
            val month = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_MONTH))
            val day = cursor.getInt(cursor.getColumnIndex(SQLText.T_DIARY_DAY))
            val date= MyDate(year, month, day)
            val title = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_TITLE))
            val content = cursor.getString(cursor.getColumnIndex(SQLText.T_DIARY_CONTENT))
            val diary = MyDiary(id, date, title, content)
            diaryList.add(diary)
        }
        return diaryList
    }

    fun deleteTable() {
        val sqLiteDatabase = writableDatabase
        sqLiteDatabase.delete(DB_TABLE_DIARY, null, null)
    }

    fun checkDay(date: MyDate): Boolean {
        val dateList: MutableList<MyDate> = mutableListOf()
        var i = 0
        val sqLiteDatabase = readableDatabase
        val sql =
            "SELECT * FROM " + DB_TABLE_DIARY + " WHERE ${SQLText.T_DIARY_YEAR} = ${date.year} AND ${SQLText.T_DIARY_MONTH}= ${date.month} AND ${SQLText.T_DIARY_DAY} = ${date.day}"
        val cursor = sqLiteDatabase.query(
            false,
            DB_TABLE_DIARY,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            i++
        }
        return i != 0
    }
    companion object {
        const val DB_NAME = "Diary.db"
        const val DB_TABLE_DIARY = "Diary"
        const val DB_VERSION = 1
    }
}