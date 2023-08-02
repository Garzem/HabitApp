package com.example.newapppp.ui.typeofhabits

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFilterAdapter(parentFragment: Fragment, private val fragments: List<Fragment>) :
    FragmentStateAdapter(parentFragment) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> fragments[0]
        1 -> fragments[1]
        else -> throw Throwable()
    }
}