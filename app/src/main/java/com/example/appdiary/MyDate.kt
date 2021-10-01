package com.example.appdiary

import java.text.SimpleDateFormat
import java.util.*

class MyDate(
    var year: Int,
    var month: Int,
    var day: Int
) {
    override fun toString(): String {
        var df = SimpleDateFormat("yyyy/MM/dd")
        var calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return df.format(calendar.time)
    }
}