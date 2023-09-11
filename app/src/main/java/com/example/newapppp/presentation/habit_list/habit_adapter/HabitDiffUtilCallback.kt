package com.example.newapppp.presentation.habit_list.habit_adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.newapppp.domain.model.HabitUI

class HabitDiffUtilCallback: DiffUtil.ItemCallback<HabitUI>() {
    override fun areItemsTheSame(oldItem: HabitUI, newItem: HabitUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HabitUI, newItem: HabitUI): Boolean {
        return oldItem == newItem
    }
}