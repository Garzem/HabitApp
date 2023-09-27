package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.DateHelper
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.presentation.habit_list.Message
import java.time.LocalDate
import javax.inject.Inject

class GetHabitUpdatedMessageUseCase @Inject constructor(private val dateHelper: DateHelper) {

    operator fun invoke(habit: Habit, date: Long): Message {
        val periodStartLong = dateHelper.getStartDate(date, habit.count)

        val currentDate = LocalDate.ofEpochDay(date)
        val periodStart = LocalDate.ofEpochDay(periodStartLong)
        val doneDates = habit.doneDates.map { LocalDate.ofEpochDay(it) }

        val executedInPeriod =
            doneDates.count {
                it.isEqual(periodStart) ||
                        (it.isAfter(periodStart) && it.isEqual(currentDate))
                        || (it.isAfter(periodStart) && it.isBefore(currentDate))
            }
        val remainingExecutions = habit.frequency - executedInPeriod

        return when {
            remainingExecutions > 0 && habit.type == HabitType.BAD -> {
                Message.TimesLeftForBadHabit(remainingExecutions)
            }

            remainingExecutions == 0 && habit.type == HabitType.BAD -> {
                Message.StopDoingBadHabit
            }

            remainingExecutions > 0 && habit.type == HabitType.GOOD -> {
                Message.TimesLeftForGoodHabit(remainingExecutions)
            }

            remainingExecutions == 0 && habit.type == HabitType.GOOD -> {
                Message.MetOrExceededGoodHabit
            }

            else -> {
                Message.Error
            }
        }
    }
}