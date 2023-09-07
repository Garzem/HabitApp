package com.example.newapppp.presentation.habit_list.habit_adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class HabitViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    fun bind(habit: Habit) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            creationDate.text = habit.creationDate.toString()
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
