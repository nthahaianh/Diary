package com.example.appdiary.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appdiary.Fragment1
import java.util.*

class ViewPagerAdapter(
    var fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {
    var listFragment: MutableList<Fragment1> = mutableListOf()

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

    fun setLists(list: MutableList<Fragment1>) {
        if (listFragment != null) for (i in 0 until listFragment.size) {
            fm.beginTransaction().remove(listFragment[i])
        }
        listFragment = list
    }

    fun update(startWeek: Int,month: Int,year: Int){
        var calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        listFragment[1].updateCalendar(startWeek,month, year)
        calendar.add(Calendar.MONTH,-1)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        listFragment[0].updateCalendar(startWeek,month, year)
        calendar.add(Calendar.MONTH,2)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        listFragment[2].updateCalendar(startWeek,month, year)
    }
}