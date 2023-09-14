package com.example.newapppp.presentation.habit_list.habit_adapter

import android.view.animation.AlphaAnimation
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper

class HabitUIViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val openDoneDatesDialog: () -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper,
    private val habitColorMapper: HabitColorMapper
) : RecyclerView.ViewHolder(binding.root) {

    private var isButtonsVisible = false

    fun bind(habit: HabitUI) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            creationDate.text = habit.creationDate
            colorMain.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            colorSupport.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            priority.text = habitPriorityMapper.getPriorityName(habit.priority)
            frequency.text = habit.frequency.toString()

            val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
            val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)

            fadeInAnimation.duration = 300
            fadeOutAnimation.duration = 300

            root.setOnClickListener {
                isButtonsVisible = !isButtonsVisible
                if (isButtonsVisible) {
                    editHabitButton.isVisible = true
                    markDayButton.isVisible = true
                    editHabitButton.startAnimation(fadeInAnimation)
                    markDayButton.startAnimation(fadeInAnimation)
                } else {
                    editHabitButton.isVisible = false
                    markDayButton.isVisible = false
                    editHabitButton.startAnimation(fadeOutAnimation)
                    markDayButton.startAnimation(fadeOutAnimation)
                }
            }
            editHabitButton.setOnClickListener {
                openHabitClick(habit.id)
            }
            markDayButton.setOnClickListener {
                openDoneDatesDialog()
            }
        }
    }
}
