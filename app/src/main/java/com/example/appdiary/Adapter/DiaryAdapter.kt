package com.example.appdiary.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appdiary.Model.MyDiary
import com.example.appdiary.R

class DiaryAdapter(private val list: MutableList<MyDiary>) :
    RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    lateinit var itemClick: (position: Int) -> Unit
    fun setCallBack(click: (position: Int) -> Unit) {
        itemClick = click
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var diary = list[position]
        holder.tvDay.text = diary.myDate.toString()
        holder.tvTitle.text = diary.title
        holder.tvContent.text = diary.content
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDay: TextView = view.findViewById(R.id.item_diary_tvDay)
        var tvTitle: TextView = view.findViewById(R.id.item_diary_tvTitle)
        var tvContent: TextView = view.findViewById(R.id.item_diary_tvContent)

        init {
            view.setOnClickListener {
                var position = adapterPosition
                itemClick.invoke(position)
            }
        }
    }
}