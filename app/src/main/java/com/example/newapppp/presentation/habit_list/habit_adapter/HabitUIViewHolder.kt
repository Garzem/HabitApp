package com.example.newapppp.presentation.habit_list.habit_adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper
import java.time.LocalDate
import java.time.ZoneOffset

class HabitUIViewHolder(
    private val binding: ItemLayoutBinding,
    private val openHabitClick: (String) -> Unit,
    private val openDoneDatesDialog: (String) -> Unit,
    private val habitPriorityMapper: HabitPriorityMapper,
    private val habitColorMapper: HabitColorMapper
) : RecyclerView.ViewHolder(binding.root) {

    private var isButtonsVisible = false

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun bind(habit: HabitUI) {
        binding.apply {
            title.text = habit.title
            description.text = habit.description
            creationDate.text = habit.creationDate
            colorMain.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            colorSupport.setBackgroundResource(habitColorMapper.getBackGroundResId(habit.color))
            priority.text = habitPriorityMapper.getPriorityName(habit.priority)
            frequency.text = habit.frequency.toString()
            doneDatesCounter.text = "Выполнено дней: ${habit.doneDates.size}"

            val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
            val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f)

            fadeInAnimation.duration = 300
            fadeOutAnimation.duration = 300

            root.setOnClickListener {
                isButtonsVisible = !isButtonsVisible
                if (isButtonsVisible) {
                    editHabitButton.isVisible = true
                    if (habit.doneDates.isNotEmpty()) {
                        if (habit.doneDates.last() != LocalDate.now().atStartOfDay().toEpochSecond(
                                ZoneOffset.UTC)) {
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
                        if (habit.doneDates.last() != LocalDate.now().atStartOfDay().toEpochSecond(
                                ZoneOffset.UTC)) {
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
