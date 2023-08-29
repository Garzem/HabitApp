package com.example.newapppp.ui.habit_list.habit_adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.data.habit_local.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.ui.habit_list.HabitPriorityMapper

class HabitViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: Habit) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            creationDate.text = habit.creationDate.toString()
            colorMain.setBackgroundResource(habit.color.getBackGroundResId())
            colorSupport.setBackgroundResource(habit.color.getBackGroundResId())
            priority.text = habitPriorityMapper.getPriorityName(habit.priority)
            frequency.text = habit.frequency.toString()
            root.setOnClickListener {
                openHabitClick(habit.id)
            }
        }
    }
}
