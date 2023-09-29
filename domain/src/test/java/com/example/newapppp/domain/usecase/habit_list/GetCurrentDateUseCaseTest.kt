package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.DateHelper
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.mockito.Mockito.mock

internal class GetCurrentDateUseCaseTest {

    private val dateHelper = mock<DateHelper>()

    @Test
    fun getCurrentDateUseCase_invoke_returnsCurrentDate() {

        val existingDate = 14234L

        Mockito.`when`(dateHelper.getCurrentDate()).thenReturn(existingDate)

        val useCase = GetCurrentDateUseCase(dateHelper = dateHelper)
        val actual = useCase()

        val expected = 14234L

        Assertions.assertEquals(expected, actual)
    }

}