package com.example.newapppp.ui.habits

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFilterAdapter (activity: AppCompatActivity, private val fragments: List<Fragment>) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> fragments[0]
        1 -> fragments[1]
        else -> throw Throwable()
        //???можно ли написать else -> Throwable()
    }
}