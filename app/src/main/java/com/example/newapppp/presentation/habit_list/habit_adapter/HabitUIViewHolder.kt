package com.example.newapppp.presentation.habit_list.habit_adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper

class HabitUIViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper,
    private val habitColorMapper: HabitColorMapper
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: HabitUI) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            creationDate.text = habit.creationDate
            colorMain.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            colorSupport.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            priority.text = habitPriorityMapper.getPriorityName(habit.priority)
            frequency.text = habit.frequency.toString()
            root.setOnClickListener {
                openHabitClick(habit.id)
            }
        }
    }
}
