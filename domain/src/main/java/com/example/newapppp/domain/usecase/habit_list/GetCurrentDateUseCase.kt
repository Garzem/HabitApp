package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.DateHelper
import javax.inject.Inject

class GetCurrentDateUseCase @Inject constructor(private val dateHelper: DateHelper) {

    operator fun invoke(): Long {
        return dateHelper.getCurrentDate()
    }
}