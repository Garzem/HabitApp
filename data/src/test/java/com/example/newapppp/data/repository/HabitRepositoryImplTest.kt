package com.example.newapppp.data.repository

import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.HabitApi
import com.example.newapppp.data.remote.NetworkRetry
import com.example.newapppp.data.remote.model.DeleteHabitJson
import com.example.newapppp.data.remote.model.HabitUidJson
import com.example.newapppp.data.remote.model.PutHabitJson
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDate

internal class HabitRepositoryImplTest {

    private lateinit var habitRepository: HabitRepositoryImpl

    private val api = mock<HabitApi>()
    private val habitDao = mock<HabitDao>()
    private val networkRetry = mock<NetworkRetry>()
    private val putHabitJson = mock<PutHabitJson>()
    private val token = "token_to_server"

    @Before
    fun setUp() {
        habitRepository = HabitRepositoryImpl(api, habitDao, networkRetry)
    }

//    @Test
//    fun `saveOrUpdateHabit should save habit to database, server and update UID`() = runTest {
//        // Given
//        val habitId = getMockHabitId()
//        val habitSave = getMockHabitSave()
//        val habitEntity = getHabitEntity()
//        val habitUidJson = getHabitUidJson()
//        `when`(api.putHabit(token, putHabitJson)).thenReturn(habitUidJson)
//
//        //When
//        habitRepository.saveOrUpdateHabit(habitSave, habitId)
//
//        // Then
//        verify(habitDao).upsertHabit(habitEntity)
//        verify(habitDao).upsertHabit(habitEntity.copy(uid = habitUidJson.uid))
//    }

    @Test
    fun `deleteHabit should mark habit as deleted if UID is not null and delete all deleted habits from db and server`() =
        runTest {
            // Given
            val habit = getMockHabitWithUid()
            val habitEntityDeleted = getMockHabitEntityDeleted()
            `when`(habitDao.getAllDeletedHabits()).thenReturn(listOf(habitEntityDeleted))

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao).upsertHabit(habitEntityDeleted)
            verify(api).deleteHabit(token, DeleteHabitJson(habit.uid!!))
            verify(habitDao).deleteHabitById(habit.id)
        }

    private fun getHabitUidJson(): HabitUidJson {
        return HabitUidJson(
            uid = "new_uid"
        )
    }

    private fun getHabitEntity(): HabitEntity {
        return HabitEntity(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = HabitType.GOOD,
            doneDates = listOf(LocalDate.of(2023, 9, 10).toEpochDay()),
            count = HabitCount.MONTH,
            frequency = 3,
            deleted = false
        )
    }

    private fun getMockHabitSave(): HabitSave {
        return HabitSave(
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = HabitType.GOOD,
            doneDates = listOf(LocalDate.of(2023, 9, 10).toEpochDay()),
            count = HabitCount.MONTH,
            frequency = 3
        )
    }

    private fun getMockHabitId(): String {
        return "habit_id"
    }

    private fun getMockHabitEntityDeleted(): HabitEntity {
        return HabitEntity(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = HabitType.GOOD,
            doneDates = listOf(LocalDate.of(2023, 9, 10).toEpochDay()),
            count = HabitCount.MONTH,
            frequency = 3,
            deleted = true
        )
    }


    private fun getMockHabitWithUid(): Habit {
        return Habit(
            id = "id",
            uid = "uid",
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.BLUE,
            priority = HabitPriority.HIGH,
            type = HabitType.GOOD,
            doneDates = listOf(LocalDate.of(2023, 9, 10).toEpochDay()),
            count = HabitCount.MONTH,
            frequency = 3
        )
    }
}