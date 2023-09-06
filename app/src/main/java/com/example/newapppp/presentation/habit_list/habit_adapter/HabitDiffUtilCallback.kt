package com.example.newapppp.presentation.habit_list.habit_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.newapppp.domain.model.Habit

class HabitDiffUtilCallback: DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }
}