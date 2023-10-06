package com.example.newapppp.data.repository

import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.HabitApi
import com.example.newapppp.data.remote.NetworkRetry
import com.example.newapppp.data.remote.model.GetHabitJson
import com.example.newapppp.data.remote.model.HabitUidJson
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.domain.model.HabitColor
import com.example.newapppp.domain.model.HabitCount
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.model.HabitSave
import com.example.newapppp.domain.model.HabitType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer

internal class HabitRepositoryImplTest {

    private lateinit var habitRepository: HabitRepositoryImpl

    private val api = mock<HabitApi>()
    private val habitDao = mock<HabitDao>()
    private val networkRetry = mock<NetworkRetry>()

    @Before
    fun setUp() {
        habitRepository = HabitRepositoryImpl(api, habitDao, networkRetry)
    }

    @Test
    fun `getHabitListFlow should get habitEntity from db, convert and return habit`() = runTest {
        // Given
        val habitEntity = getMockHabitEntity(
            uid = null,
            deleted = false
        )
        val expected = listOf(
            getMockHabit(
                uid = null
            )
        )
        `when`(habitDao.getAllHabitsFlow()).thenReturn(flowOf(listOf(habitEntity)))

        //When
        val actual = habitRepository.getHabitListFlow().first()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `saveOrUpdateHabit should save habit to database, server and update UID`() = runTest {
        // Given
        val habitId = getMockHabitId()
        val habitSave = getMockHabitSave()
        val habitEntity = getMockHabitEntity(
            uid = null,
            deleted = false
        )
        val habitUidJson = getMockHabitUidJson(
            uid = "uid"
        )
        `when`(habitDao.upsertHabit(habitEntity)).thenReturn(Unit)
        `when`(api.putHabit(any(), any())).thenReturn(habitUidJson)

        //When
        habitRepository.saveOrUpdateHabit(habitSave, habitId)

        // Then
        verify(habitDao, times(1)).upsertHabit(habitEntity)
        verify(api, times(1)).putHabit(any(), any())
        verify(habitDao, times(1)).upsertHabit(
            habitEntity.copy(
                uid = habitUidJson.uid
            )
        )
    }

    @Test
    fun `saveOrUpdateHabit should save habit to database and handle when there isn't internet connection`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitSave = getMockHabitSave()
            val habitEntity = getMockHabitEntity(
                uid = null,
                deleted = false
            )
            `when`(habitDao.upsertHabit(habitEntity)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api)
                .putHabit(any(), any())

            //When
            habitRepository.saveOrUpdateHabit(habitSave, habitId)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntity)
            verify(api).putHabit(any(), any())
        }


    @Test
    fun `deleteHabit should mark habit as deleted because UID isn't null and delete all deleted habits from db and server`() =
        runTest {
            // Given
            val habit = getMockHabit(
                uid = "uid"
            )
            val habitEntityDeleted = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            `when`(api.deleteHabit(any(), any())).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted)
            verify(api, times(1)).deleteHabit(any(), any())
            verify(habitDao, times(1)).deleteHabitById(habit.id)
        }

    @Test
    fun `deleteHabit should mark habit as deleted because UID isn't null, delete all deleted habits from db and handle when there isn't internet connection`() =
        runTest {
            // Given
            val habit = getMockHabit(
                uid = "uid"
            )
            val habitEntityDeleted = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api)
                .deleteHabit(any(), any())

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted)
            verify(api, times(1)).deleteHabit(any(), any())
        }

    @Test
    fun `deleteHabit should delete habit without UID from db by ID`() =
        runTest {
            // Given
            val habit = getMockHabit(
                uid = null
            )
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).deleteHabitById(habit.id)
        }

    @Test
    fun `deleteAllHabits should mark habits as deleted because UIDs aren't null and delete all deleted habits from db and server`() =
        runTest {
            // Given
            val habit = getMockHabit(
                uid = "uid"
            )
            val habitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = false
            )
            val habitEntityDeleted = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity, habitEntity))
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            `when`(api.deleteHabit(any(), any())).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(2)).upsertHabit(habitEntityDeleted)
            verify(api, times(2)).deleteHabit(any(), any())
            verify(habitDao, times(2)).deleteHabitById(habit.id)
        }

    @Test
    fun `deleteAllHabits should mark habits as deleted because UIDs aren't null, delete all deleted habits from db and handle when there isn't internet connection`() =
        runTest {
            // Given
            val habitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = false
            )
            val habitEntityDeleted = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity, habitEntity))
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api)
                .deleteHabit(any(), any())

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted)
            verify(api, times(1)).deleteHabit(any(), any())
        }

    @Test
    fun `deleteAllHabits should delete habits without UIDs from db by IDs`() =
        runTest {
            // Given
            val habit = getMockHabit(
                uid = null
            )
            val habitEntity = getMockHabitEntity(
                uid = null,
                deleted = false
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity, habitEntity))
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(2)).deleteHabitById(habit.id)
        }


    @Test
    fun `updateHabitDates should update field doneDates in habit and save it in db and server`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = false
            )
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(
                    doneDates = habitEntity.doneDates + doneDate
                )
            val habit = getMockHabit(
                uid = "uid"
            )
            val expected = habit.copy(
                doneDates = habit.doneDates + doneDate
            )
            `when`(habitDao.getHabitById(habitId)).thenReturn(habitEntity)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)
            `when`(api.postHabit(any(), any())).thenReturn(Unit)

            // When
            val actual = habitRepository.updateHabitDates(habitId, doneDate)

            // Then
            verify(habitDao, times(1)).getHabitById(habitId)
            verify(habitDao, times(1)).upsertHabit(updatedHabitEntity)
            verify(api, times(1)).postHabit(any(), any())
            assertEquals(expected, actual)
        }

    @Test
    fun `updateHabitDates should update field doneDates in habit and save it in db and handle when there isn't internet connection`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = false
            )
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(
                    doneDates = habitEntity.doneDates + doneDate
                )
            val habit = getMockHabit(
                uid = "uid"
            )
            val expected = habit.copy(
                doneDates = habit.doneDates + doneDate
            )
            `when`(habitDao.getHabitById(habitId)).thenReturn(habitEntity)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api)
                .postHabit(any(), any())

            // When
            val actual = habitRepository.updateHabitDates(habitId, doneDate)

            // Then
            verify(habitDao, times(1)).getHabitById(habitId)
            verify(habitDao, times(1)).upsertHabit(updatedHabitEntity)
            verify(api, times(1)).postHabit(any(), any())
            assertEquals(expected, actual)
        }

    @Test
    fun `updateHabitDates should update field doneDates in habit where UID is null and save it only in db`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(
                uid = null,
                deleted = false
            )
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(
                    doneDates = habitEntity.doneDates + doneDate
                )
            val habit = getMockHabit(
                uid = null
            )
            val expected = habit.copy(
                doneDates = habit.doneDates + doneDate
            )
            `when`(habitDao.getHabitById(habitId)).thenReturn(habitEntity)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)

            // When
            val actual = habitRepository.updateHabitDates(habitId, doneDate)

            // Then
            verify(habitDao, times(1)).getHabitById(habitId)
            verify(habitDao, times(1)).upsertHabit(updatedHabitEntity)
            assertEquals(expected, actual)
        }


    // ???
    @Test
    fun `fetchHabitList should get habits from server and save those that aren't in db into db`() =
        runTest {
            // Given
            val habitJsonList = listOf(
                getMockGetHabitJson(
                    uid = "uid"
                ),
                getMockGetHabitJson(
                    uid = "uid1"
                )
            )
            val habitEntityList = listOf(
                getMockHabitEntity(
                    uid = "uid",
                    deleted = false
                )
            )
            `when`(networkRetry.commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull()))
                .thenReturn(habitJsonList)
            `when`(habitDao.getAllHabits()).thenReturn(habitEntityList)
            `when`(habitDao.upsertHabitList(any())).thenReturn(Unit)


            // When
            habitRepository.fetchHabitList()

            // Then
            verify(networkRetry, times(1))
                .commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull())
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(1)).upsertHabitList(any())
        }

    @Test
    fun `fetchHabitList shouldn't save anything in db, because habitJsonList is empty`() =
        runTest {
            // Given
            val habitJsonList = emptyList<GetHabitJson>()
            val habitEntityList = listOf(
                getMockHabitEntity(
                    uid = "uid",
                    deleted = false
                )
            )
            `when`(networkRetry.commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull()))
                .thenReturn(habitJsonList)
            `when`(habitDao.getAllHabits()).thenReturn(habitEntityList)

            // When
            habitRepository.fetchHabitList()

            // Then
            verify(networkRetry, times(1))
                .commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull())
            verify(habitDao, times(1)).getAllHabits()
        }

    @Test
    fun `fetchHabitList shouldn't save anything in db, because habitJsonList(response) is null`() =
        runTest {
            // Given
            val habitJsonList = null
            val habitEntityList = listOf(
                getMockHabitEntity(
                    uid = "uid",
                    deleted = false
                )
            )
            `when`(networkRetry.commonRetrying<List<GetHabitJson>?>(anyOrNull(), anyOrNull()))
                .thenReturn(habitJsonList)
            `when`(habitDao.getAllHabits()).thenReturn(habitEntityList)

            // When
            habitRepository.fetchHabitList()

            // Then
            verify(networkRetry, times(1))
                .commonRetrying<List<GetHabitJson>?>(anyOrNull(), anyOrNull())
            verify(habitDao, times(1)).getAllHabits()
        }

    @Test
    fun `deleteOfflineDeletedHabits should delete all deleted habits with UIDs from db and server`() =
        runTest {
            // Given
            val deletedHabitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            val response = Unit
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(
                    deletedHabitEntity,
                    deletedHabitEntity
                )
            )
            `when`(networkRetry.commonRetrying<Unit>(anyOrNull(), anyOrNull()))
                .thenReturn(response)
            `when`(habitDao.deleteHabitById(deletedHabitEntity.id)).thenReturn(Unit)

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
            verify(networkRetry, times(2))
                .commonRetrying<Unit>(anyOrNull(), anyOrNull())
            verify(habitDao, times(2)).deleteHabitById(deletedHabitEntity.id)
        }

    @Test
    fun `deleteOfflineDeletedHabits shouldn't delete any deleted habits because response is null`() =
        runTest {
            // Given
            val deletedHabitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            val response = null
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(
                    deletedHabitEntity,
                    deletedHabitEntity
                )
            )
            `when`(networkRetry.commonRetrying<Unit?>(anyOrNull(), anyOrNull()))
                .thenReturn(response)

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
            verify(networkRetry, times(2))
                .commonRetrying<Unit?>(anyOrNull(), anyOrNull())
        }


    // ???
    @Test
    fun `deleteOfflineDeletedHabits shouldn't delete any deleted habits because UIDs are null`() =
        runTest {
            // Given
            val deletedHabitEntity = getMockHabitEntity(
                uid = null,
                deleted = true
            )
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(
                    deletedHabitEntity,
                    deletedHabitEntity
                )
            )

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
        }

    @Test
    fun `putOfflineHabitList should save habits which were saved only in db into server and save their UIDs in db`() =
        runTest {
            // When
            val habitJson = getMockGetHabitJson(
                uid = "uid"
            )
            val habitEntity = getMockHabitEntity(
                uid = null,
                deleted = false
            )
            val habitUidJson = getMockHabitUidJson(
                uid = "uid1"
            )
            val updatedHabitEntity = habitEntity.copy(
                uid = habitUidJson.uid
            )
            `when`(networkRetry.commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull()))
                .thenReturn(listOf(habitJson, habitJson))
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity, habitEntity))
            `when`(networkRetry.commonRetrying<HabitUidJson>(anyOrNull(), anyOrNull()))
                .thenReturn(habitUidJson)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)

            // When
            habitRepository.putOfflineHabitList()

            // Then
            verify(networkRetry, times(1))
                .commonRetrying<List<GetHabitJson>>(anyOrNull(), anyOrNull())
            verify(habitDao, times(1)).getAllHabits()
            verify(networkRetry, times(2))
                .commonRetrying<HabitUidJson>(anyOrNull(), anyOrNull())
            verify(habitDao, times(2)).upsertHabit(updatedHabitEntity)
        }

    @Test
    fun `putOfflineHabitList shouldn't save habits into server because response is null`() =
        runTest {
            // When
            val response = null
            `when`(networkRetry.commonRetrying<List<GetHabitJson>?>(anyOrNull(), anyOrNull()))
                .thenReturn(response)

            // When
            habitRepository.putOfflineHabitList()

            // Then
            verify(networkRetry, times(1))
                .commonRetrying<List<GetHabitJson>?>(anyOrNull(), anyOrNull())
        }


    private fun getMockHabitUidJson(uid: String): HabitUidJson {
        return HabitUidJson(
            uid = uid
        )
    }

    private fun getMockGetHabitJson(uid: String): GetHabitJson {
        return GetHabitJson(
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = 0,
            priority = 0,
            type = 0,
            doneDates = listOf(19610L), // 10/9/2023
            count = 0,
            frequency = 3
        )
    }

    private fun getMockHabitSave(): HabitSave {
        return HabitSave(
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = HabitType.GOOD,
            doneDates = listOf(19610L),
            count = HabitCount.WEEK,
            frequency = 3
        )
    }

    private fun getMockHabit(uid: String?): Habit {
        return Habit(
            id = "id",
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = HabitType.GOOD,
            doneDates = listOf(19610L), // 10/9/2023
            count = HabitCount.WEEK,
            frequency = 3
        )
    }

    private fun getMockHabitEntity(uid: String?, deleted: Boolean): HabitEntity {
        return HabitEntity(
            id = "id",
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = HabitType.GOOD,
            doneDates = listOf(19610L), // 10/9/2023
            count = HabitCount.WEEK,
            frequency = 3,
            deleted = deleted
        )
    }

    private fun getMockHabitId(): String {
        return "id"
    }
}