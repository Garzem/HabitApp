package com.example.newapppp.domain.usecase.habit_list

import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

internal class GetHabitListUseCaseTest {

    private val habitRepository = mock<HabitRepository>()
    private lateinit var getHabitListUseCase: GetHabitListUseCase

    @Before
    fun setUp() {
        getHabitListUseCase = GetHabitListUseCase(habitRepository)
    }

    @Test
    fun `return correct filtered and sorted habitList`() = runTest {
        // Given
        val expected = listOf(
            getMockHabit(HabitType.BAD, 123451L),
            getMockHabit(HabitType.BAD, 123452L),
            getMockHabit(HabitType.BAD, 123453L)
        )
        `when`(habitRepository.getHabitListFlow()).thenReturn(
            flowOf(
                listOf(
                    getMockHabit(HabitType.BAD, 123451L),
                    getMockHabit(HabitType.GOOD, 123450L),
                    getMockHabit(HabitType.BAD, 123453L),
                    getMockHabit(HabitType.BAD, 123452L)
                )
            )
        )

        // When
        val actual: List<Habit> = getHabitListUseCase(HabitType.BAD).first()

        // Then
        assertEquals(expected, actual)
    }

    private fun getMockHabit(type: HabitType, creationDate: Long): Habit {
        return Habit(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = creationDate,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = type,
            doneDates = emptyList(),
            count = HabitCount.WEEK,
            frequency = 2
        )
    }
}