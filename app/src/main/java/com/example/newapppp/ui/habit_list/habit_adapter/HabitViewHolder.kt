package com.example.newapppp.ui.habit_list.habit_adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.data.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.ui.home.HabitPriorityMapper

class HabitViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: Habit) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            period.text = habit.period
            colorMain.setBackgroundResource(habit.color.getBackGroundResId())
            colorSupport.setBackgroundResource(habit.color.getBackGroundResId())
            priority.text = habitPriorityMapper.getPriorityName(habit.priority)
            quantity.text = habit.quantity
            root.setOnClickListener {
                openHabitClick(habit.id)
            }
        }
    }
}
