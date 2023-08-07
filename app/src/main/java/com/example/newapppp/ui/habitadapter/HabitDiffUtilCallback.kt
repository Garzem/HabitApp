package com.example.newapppp.ui.habitadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.newapppp.data.Habit

class HabitDiffUtilCallback: DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }
}