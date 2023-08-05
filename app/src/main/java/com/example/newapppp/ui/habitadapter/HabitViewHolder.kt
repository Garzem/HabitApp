package com.example.newapppp.ui.habitadapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.database.HabitEntity
import com.example.newapppp.databinding.ItemLayoutBinding

class HabitViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (HabitEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: HabitEntity) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            period.text = habit.period
            colorMain.setBackgroundResource(habit.color.getBackGroundResId())
            colorSupport.setBackgroundResource(habit.color.getBackGroundResId())
            priority.text = habit.getPriorityName()
            quantity.text = habit.quantity
            root.setOnClickListener {
                openHabitClick(habit)
            }
        }
    }
}
