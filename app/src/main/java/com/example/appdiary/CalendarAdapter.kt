package com.example.appdiary

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private var list: MutableList<MyDate>,
    private val thisMonth: Int,
    var sharedPreferences: SharedPreferences
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var position0 = -1
    var position1 = -1
    var position2 = -1
//    lateinit var sqlHelper:SQLHelper
//    lateinit var diaryList:List<MyDiary>
    lateinit var context: Context
    lateinit var itemClick:(position:Int)->Unit
    fun setCallBack(click:(position:Int)->Unit){
        itemClick = click
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day1, parent, false)
        context = parent.context
//        sqlHelper = SQLHelper(context)
//        diaryList = sqlHelper.getAll()
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.16666666).toInt()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var day = list[position]
        holder.tvDay.text = day.day.toString()
        if (day.month != thisMonth) {
            holder.tvDay.setTextColor(Color.rgb(100, 100, 100))
            holder.itemView.setBackgroundResource(R.drawable.background2)
        } else {
            holder.tvDay.setTextColor(Color.rgb(0, 0, 0))
            holder.itemView.setBackgroundResource(R.drawable.background1)
            //doi background khi co nhat ky
//            for (itemDiary in diaryList){
//                if(day.day==itemDiary.myDate.day&& day.month==itemDiary.myDate.month && day.year==itemDiary.myDate.year){
//                    holder.itemView.setBackgroundResource(R.drawable.background5)
//                }
//            }
        }
        if (position == position1) {
            holder.itemView.setBackgroundResource(R.drawable.background3)
//        } else if (position == position2) {
//            holder.tvDay.setBackgroundColor(
//                Color.rgb(
//                    java.util.Random().nextInt(255),
//                    java.util.Random().nextInt(255),
//                    java.util.Random().nextInt(255)
//                )
//            )
        } else if (day.year == sharedPreferences.getInt("spYear", 0)
            && day.month == sharedPreferences.getInt("spMonth", 0)
            && day.day == sharedPreferences.getInt("spDay", 0)
        ) {
            holder.tvDay.setBackgroundResource(R.drawable.background3)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDay: TextView = view.findViewById(R.id.item_day_tvDay)
        init {
            var gestureDetector = GestureDetector(context, GestureListener())
            itemView.setOnTouchListener { _, event ->
                position0 = adapterPosition
                gestureDetector.onTouchEvent(event)
            }
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            position1 = position0
            position2 = -1
            val editor = sharedPreferences.edit()
            editor?.putInt("spYear", list[position0].year)
            editor?.putInt("spMonth", list[position0].month)
            editor?.putInt("spDay", list[position0].day)
            editor?.apply()
            notifyDataSetChanged()
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            position2 = position0
            position1 = -1
            val position: Int = position0
            itemClick.invoke(position)
            val editor = sharedPreferences.edit()
            editor.putInt("spYear", list[position0].year)
            editor.putInt("spMonth", list[position0].month)
            editor.putInt("spDay", list[position0].day)
            editor.apply()
            notifyDataSetChanged()
            return true
        }
    }
}