package com.example.newapppp.presentation.habit_list.habit_adapter

import android.content.Context
import android.os.Build
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.R
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.mapper.HabitColorMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitCountMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import java.time.LocalDate

class HabitUIViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val openDoneDatesDialog: (String) -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper,
    private val habitCountMapper: HabitCountMapper,
    private val habitColorMapper: HabitColorMapper,
    private val context: Context
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
            performanceStandard.text = context.getString(
                R.string.performance_standard_adapter, habit.frequency, habitCountMapper
                    .getCountNameHabitListFragment(habit.count))
            doneDatesCounter.text = context.getString(
                R.string.done_dates_adapter, habit.doneDates.size)

            val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
            val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)

            fadeInAnimation.duration = 300
            fadeOutAnimation.duration = 300

            root.setOnClickListener {
                isButtonsVisible = !isButtonsVisible
                if (isButtonsVisible) {
                    editHabitButton.isVisible = true
                    if (habit.doneDates.isNotEmpty()) {
                        if (!habit.doneDates.any { it == LocalDate.now().toEpochDay() }) {
                            openMarkDayButton.isVisible = true
                            openMarkDayButton.startAnimation(fadeInAnimation)
                        } else {
                            checkmark.isVisible = true
                            checkmark.startAnimation(fadeInAnimation)
                        }
                    } else {
                        openMarkDayButton.isVisible = true
                        openMarkDayButton.startAnimation(fadeInAnimation)
                    }
                    editHabitButton.startAnimation(fadeInAnimation)
                    openMarkDayButton.startAnimation(fadeInAnimation)
                } else {
                    editHabitButton.isVisible = false
                    if (habit.doneDates.isNotEmpty()) {
                        if (!habit.doneDates.any { it == LocalDate.now().toEpochDay() }) {
                            openMarkDayButton.isVisible = false
                            openMarkDayButton.startAnimation(fadeOutAnimation)
                        } else {
                            checkmark.isVisible = false
                            checkmark.startAnimation(fadeOutAnimation)
                        }
                    } else {
                        openMarkDayButton.isVisible = false
                        openMarkDayButton.startAnimation(fadeOutAnimation)
                    }
                    editHabitButton.startAnimation(fadeOutAnimation)
                }
            }
            editHabitButton.setOnClickListener {
                openHabitClick(habit.id)
            }
            openMarkDayButton.setOnClickListener {
                openDoneDatesDialog(habit.id)
            }
        }
    }
}
