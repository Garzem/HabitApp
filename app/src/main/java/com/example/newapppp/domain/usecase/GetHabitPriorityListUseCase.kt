package com.example.newapppp.domain.usecase

import com.example.newapppp.domain.model.HabitPriority

class GetHabitPriorityListUseCase {

    operator fun invoke():List<String> {
        return HabitPriority.values().map {
//            HabitPriorityMapper().getPriorityName(it)
            when (it) {
                HabitPriority.CHOOSE -> "Приоритет"
                HabitPriority.LOW -> "Низкий"
                HabitPriority.MEDIUM -> "Средний"
                HabitPriority.HIGH -> "Высокий"
            }
        }
    }
}