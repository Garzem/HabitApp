package com.example.newapppp.data

import com.example.newapppp.domain.model.HabitCount
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate

internal class DateHelperImplTest {

    private lateinit var dateHelper: DateHelperImpl
    private val currentDate = LocalDate.of(2023, 9, 30).toEpochDay()

    @Before
    fun setUp() {
        dateHelper = DateHelperImpl()
    }

    @Test
    fun `return the correct start date of the week`() {
        // Given
        val period = HabitCount.WEEK
        val expectedDate = LocalDate.of(2023, 9, 25).toEpochDay()

        // When
        val actual = dateHelper.getStartDate(currentDate, period)

        // Then
        assertEquals(expectedDate, actual)
    }

    @Test
    fun `return the correct start date of the month`() {
        // Given
        val period = HabitCount.MONTH
        val expectedDate = LocalDate.of(2023, 9, 1).toEpochDay()

        // When
        val actual = dateHelper.getStartDate(currentDate, period)

        // Then
        assertEquals(expectedDate, actual)
    }

    @Test
    fun `return the correct start date of the year`() {
        // Given
        val period = HabitCount.YEAR
        val expectedDate = LocalDate.of(2023, 1, 1).toEpochDay()

        // When
        val actual = dateHelper.getStartDate(currentDate, period)

        // Then
        assertEquals(expectedDate, actual)
    }

    @Test
    fun `wrong period should throw the IllegalStateException`() {
        // Given
        val period = HabitCount.CHOOSE
        val expectedErrorMessage = "Unsupported habit count: $period"

        // When
        val actual = assertThrows(IllegalStateException::class.java) {
            dateHelper.getStartDate(currentDate, period)
        }

        // Then
        assertEquals(expectedErrorMessage, actual.message)
    }
}