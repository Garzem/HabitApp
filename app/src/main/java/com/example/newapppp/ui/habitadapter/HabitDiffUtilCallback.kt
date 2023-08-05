package com.example.newapppp.ui.habitadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.newapppp.database.HabitEntity

class HabitDiffUtilCallback: DiffUtil.ItemCallback<HabitEntity>() {
    override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
        return oldItem == newItem
    }
}