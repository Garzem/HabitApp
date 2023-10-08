package com.example.newapppp.data.repository

import com.example.newapppp.data.UuidGenerator
import com.example.newapppp.data.database.HabitDao
import com.example.newapppp.data.database.habit_local.HabitEntity
import com.example.newapppp.data.remote.HabitApi
import com.example.newapppp.data.remote.NetworkRetry
import com.example.newapppp.data.remote.model.DeleteHabitJson
import com.example.newapppp.data.remote.model.GetHabitJson
import com.example.newapppp.data.remote.model.HabitUidJson
import com.example.newapppp.data.remote.model.PostHabitJson
import com.example.newapppp.domain.Constants
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
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.internal.junit.JUnitRule
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer

internal class HabitRepositoryImplTest {

    private lateinit var habitRepository: HabitRepositoryImpl

    private val api = mock<HabitApi>()
    private val habitDao = mock<HabitDao>()
    private val networkRetry = NetworkRetry()
    private val uuidGenerator = mock<UuidGenerator>()

    @Before
    fun setUp() {
        habitRepository = HabitRepositoryImpl(api, habitDao, networkRetry, uuidGenerator)
    }

    @Test
    fun `getHabitListFlow should get habitEntity from db and return habit`() = runTest {
        // Given
        val habitEntity = getMockHabitEntity(uid = null)
        val expected = listOf(getMockHabit(uid = null))
        `when`(habitDao.getAllHabitsFlow()).thenReturn(flowOf(listOf(habitEntity)))

        //When
        val actual = habitRepository.getHabitListFlow().first()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `saveOrUpdateHabit should save habit to db and api with UID`() = runTest {
        // Given
        val habitId = getMockHabitId()
        val habitSave = getMockHabitSave()
        val habitEntity = getMockHabitEntity(uid = null)
        val habitUidJson = getMockHabitUidJson(uid = "uid")
        `when`(habitDao.upsertHabit(habitEntity)).thenReturn(Unit)
        `when`(api.putHabit(anyString(), any())).thenReturn(habitUidJson)

        //When
        habitRepository.saveOrUpdateHabit(habitSave, habitId)

        // Then
        verify(habitDao, times(1)).upsertHabit(habitEntity)
        verify(api, times(1)).putHabit(anyString(), any())
        verify(habitDao, times(1)).upsertHabit(
            habitEntity.copy(uid = habitUidJson.uid)
        )
    }

    @Test
    fun `saveOrUpdateHabit should save habit to db only when internet`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitSave = getMockHabitSave()
            val habitEntity = getMockHabitEntity(uid = null)
            `when`(habitDao.upsertHabit(habitEntity)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api).putHabit(anyString(), any())

            //When
            habitRepository.saveOrUpdateHabit(habitSave, habitId)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntity)
            verify(api).putHabit(anyString(), any())
        }


    @Test
    fun `deleteHabit should delete habit from db and api`() =
        runTest {
            // Given
            val habit = getMockHabit(uid = "uid")
            val habitEntityDeleted = getMockHabitEntity(uid = "uid", deleted = true)
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            `when`(api.deleteHabit(anyString(), any())).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted)
            verify(api, times(1)).deleteHabit(anyString(), any())
            verify(habitDao, times(1)).deleteHabitById(habit.id)
        }

    @Test
    fun `deleteHabit should delete habit from db only when no internet`() =
        runTest {
            // Given
            val habit = getMockHabit(uid = "uid")
            val habitEntityDeleted = getMockHabitEntity(uid = "uid", deleted = true)
            `when`(habitDao.upsertHabit(habitEntityDeleted)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api).deleteHabit(anyString(), any())

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted)
            verify(api, times(1)).deleteHabit(anyString(), any())
        }

    @Test
    fun `deleteHabit should delete habit without UID from db`() =
        runTest {
            // Given
            val habit = getMockHabit(uid = null)
            `when`(habitDao.deleteHabitById(habit.id)).thenReturn(Unit)

            // When
            habitRepository.deleteHabit(habit)

            // Then
            verify(habitDao, times(1)).deleteHabitById(habit.id)
        }

    @Test
    fun `deleteAllHabits should delete all deleted habits from db and api`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(id = "id1", uid = "uid1")
            val habitEntity2 = getMockHabitEntity(id = "id2", uid = "uid2")
            val habitEntityDeleted1 = getMockHabitEntity(
                id = "id1",
                uid = "uid1",
                deleted = true
            )
            val habitEntityDeleted2 = getMockHabitEntity(
                id = "id2",
                uid = "uid2",
                deleted = true
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1, habitEntity2))
            `when`(habitDao.upsertHabit(habitEntityDeleted1)).thenReturn(Unit)
            `when`(habitDao.upsertHabit(habitEntityDeleted2)).thenReturn(Unit)
            `when`(api.deleteHabit(anyString(), any())).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habitEntityDeleted1.id)).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habitEntityDeleted2.id)).thenReturn(Unit)

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted1)
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted2)
            verify(api, times(2)).deleteHabit(anyString(), any())
            verify(habitDao, times(1)).deleteHabitById(habitEntityDeleted1.id)
            verify(habitDao, times(1)).deleteHabitById(habitEntityDeleted2.id)
        }

    @Test
    fun `deleteAllHabits should delete all deleted habits from db only when no internet`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(uid = "uid1")
            val habitEntity2 = getMockHabitEntity(uid = "uid2")
            val habitEntityDeleted1 = getMockHabitEntity(
                uid = "uid1",
                deleted = true
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1, habitEntity2))
            `when`(habitDao.upsertHabit(habitEntityDeleted1)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api).deleteHabit(anyString(), any())

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(1)).upsertHabit(habitEntityDeleted1)
            verify(api, times(1)).deleteHabit(anyString(), any())
        }

    @Test
    fun `deleteAllHabits should delete habits without UIDs from db`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(
                id = "id1",
                uid = null
            )
            val habitEntity2 = getMockHabitEntity(
                id = "id2",
                uid = null
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1, habitEntity2))
            `when`(habitDao.deleteHabitById(habitEntity1.id)).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(habitEntity2.id)).thenReturn(Unit)

            // When
            habitRepository.deleteAllHabits()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(habitDao, times(1)).deleteHabitById(habitEntity1.id)
            verify(habitDao, times(1)).deleteHabitById(habitEntity2.id)
        }


    @Test
    fun `updateHabitDates should update habit doneDates in db and api`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(uid = "uid")
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(doneDates = habitEntity.doneDates + doneDate)
            val habit = getMockHabit(uid = "uid")
            val expected = habit.copy(doneDates = habit.doneDates + doneDate)
            `when`(habitDao.getHabitById(habitId)).thenReturn(habitEntity)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)
            `when`(api.postHabit(anyString(), any())).thenReturn(Unit)

            // When
            val actual = habitRepository.updateHabitDates(habitId, doneDate)

            // Then
            verify(habitDao, times(1)).getHabitById(habitId)
            verify(habitDao, times(1)).upsertHabit(updatedHabitEntity)
            verify(api, times(1)).postHabit(anyString(), any())
            assertEquals(expected, actual)
        }

    @Test
    fun `updateHabitDates should update habit doneDates in db only when no internet`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(uid = "uid")
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(doneDates = habitEntity.doneDates + doneDate)
            val habit = getMockHabit(uid = "uid")
            val expected = habit.copy(doneDates = habit.doneDates + doneDate)
            `when`(habitDao.getHabitById(habitId)).thenReturn(habitEntity)
            `when`(habitDao.upsertHabit(updatedHabitEntity)).thenReturn(Unit)
            doAnswer { throw Exception() }
                .`when`(api)
                .postHabit(anyString(), any())

            // When
            val actual = habitRepository.updateHabitDates(habitId, doneDate)

            // Then
            verify(habitDao, times(1)).getHabitById(habitId)
            verify(habitDao, times(1)).upsertHabit(updatedHabitEntity)
            verify(api, times(1)).postHabit(anyString(), any())
            assertEquals(expected, actual)
        }

    @Test
    fun `updateHabitDates should update habit doneDates in db only when UID is null`() =
        runTest {
            // Given
            val habitId = getMockHabitId()
            val habitEntity = getMockHabitEntity(uid = null)
            val doneDate = 19615L // 15/9/2023
            val updatedHabitEntity =
                habitEntity.copy(doneDates = habitEntity.doneDates + doneDate)
            val habit = getMockHabit(uid = null)
            val expected = habit.copy(doneDates = habit.doneDates + doneDate)
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
    fun `fetchHabitList should get habits from api and save into db`() =
        runTest {
            // Given
            val getHabitJson1 = getMockGetHabitJson(uid = "uid1")
            val getHabitJson2 = getMockGetHabitJson(uid = "uid2")
            val getHabitJson3 = getMockGetHabitJson(uid = "uid3")
            val habitEntity3 = getMockHabitEntity(uid = "uid3")
            val habitEntity4 = getMockHabitEntity(uid = "uid4")
            val notSavedHabitEntityList = listOf(
                getMockHabitEntity(id = "id1", uid = "uid1"),
                getMockHabitEntity(id = "id2", uid = "uid2"),
            )
            `when`(api.getHabitList(anyString())).thenReturn(
                listOf(
                    getHabitJson1,
                    getHabitJson2,
                    getHabitJson3
                )
            )
            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity3, habitEntity4))
            `when`(uuidGenerator.generateUuid()).thenReturn("id1", "id2")
            `when`(habitDao.upsertHabitList(notSavedHabitEntityList)).thenReturn(Unit)


            // When
            habitRepository.fetchHabitList()

            // Then
            verify(api, times(1)).getHabitList(anyString())
            verify(habitDao, times(1)).getAllHabits()
            verify(uuidGenerator, times(2)).generateUuid()
            verify(habitDao, times(1)).upsertHabitList(notSavedHabitEntityList)
        }

    @Test
    fun `fetchHabitList shouldn't save anything in db when habitJsonList is empty`() =
        runTest {
            // Given
            val getHabitJsonList = emptyList<GetHabitJson>()
            val habitEntityList = listOf(getMockHabitEntity(uid = "uid"))
            `when`(api.getHabitList(anyString())).thenReturn(getHabitJsonList)
            `when`(habitDao.getAllHabits()).thenReturn(habitEntityList)

            // When
            habitRepository.fetchHabitList()

            // Then
            verify(api, times(1)).getHabitList(anyString())
            verify(habitDao, times(1)).getAllHabits()
        }

    @Test
    fun `fetchHabitList shouldn't save anything in db when habitJsonList(response) is null`() =
        runTest {
            // Given
            val habitJsonList = null
            val habitEntityList = listOf(getMockHabitEntity(uid = "uid"))
            `when`(api.getHabitList(anyString())).thenReturn(habitJsonList)
            `when`(habitDao.getAllHabits()).thenReturn(habitEntityList)

            // When
            habitRepository.fetchHabitList()

            // Then
            verify(api, times(1)).getHabitList(anyString())
            verify(habitDao, times(1)).getAllHabits()
        }

    @Test
    fun `deleteOfflineDeletedHabits should delete all deleted habits from db and api`() =
        runTest {
            // Given
            val deletedHabitEntity1 = getMockHabitEntity(
                id = "id1",
                uid = "uid1",
                deleted = true
            )
            val deletedHabitEntity2 = getMockHabitEntity(
                id = "id2",
                uid = "uid2",
                deleted = true
            )
            val response = Unit
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(deletedHabitEntity1, deletedHabitEntity2)
            )
            `when`(api.deleteHabit(anyString(), any())).thenReturn(response)
            `when`(habitDao.deleteHabitById(deletedHabitEntity1.id)).thenReturn(Unit)
            `when`(habitDao.deleteHabitById(deletedHabitEntity2.id)).thenReturn(Unit)

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
            verify(api, times(2)).deleteHabit(anyString(), any())
            verify(habitDao, times(1)).deleteHabitById(deletedHabitEntity1.id)
            verify(habitDao, times(1)).deleteHabitById(deletedHabitEntity2.id)
        }

    @Test
    fun `deleteOfflineDeletedHabits shouldn't delete any deleted habits when response is null`() =
        runTest {
            // Given
            val deletedHabitEntity = getMockHabitEntity(
                uid = "uid",
                deleted = true
            )
            val response = null
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(deletedHabitEntity, deletedHabitEntity)
            )
            `when`(api.deleteHabit(anyString(), any())).thenReturn(response)

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
            verify(api, times(2)).deleteHabit(anyString(), any())
        }


    // ???
    @Test
    fun `deleteOfflineDeletedHabits shouldn't delete any deleted habits when UIDs are null`() =
        runTest {
            // Given
            val deletedHabitEntity = getMockHabitEntity(
                uid = null,
                deleted = true
            )
            `when`(habitDao.getAllDeletedHabits()).thenReturn(
                listOf(deletedHabitEntity, deletedHabitEntity)
            )

            // When
            habitRepository.deleteOfflineDeletedHabits()

            // Then
            verify(habitDao, times(1)).getAllDeletedHabits()
        }

    @Test
    fun `putOfflineHabitList should send local habits to api and save their UIDs in db`() =
        runTest {
            // When
            val getHabitJson1 = getMockGetHabitJson(uid = "uid1")
            val getHabitJson2 = getMockGetHabitJson(uid = "uid2")
            val getHabitJson3 = getMockGetHabitJson(uid = "uid3")
            val habitEntity3 = getMockHabitEntity(uid = "uid3")
            val habitEntity4 = getMockHabitEntity(uid = null)
            val habitEntity5 = getMockHabitEntity(uid = null)
            val habitUidJson4 = getMockHabitUidJson(uid = "uid4")
            val habitUidJson5 = getMockHabitUidJson(uid = "uid5")
            val habitEntity4WithUid = habitEntity4.copy(uid = habitUidJson4.uid)
            val habitEntity5WithUid = habitEntity5.copy(uid = habitUidJson5.uid)

            `when`(api.getHabitList(anyString()))
                .thenReturn(listOf(getHabitJson1, getHabitJson2, getHabitJson3))
            `when`(habitDao.getAllHabits())
                .thenReturn(listOf(habitEntity3, habitEntity4, habitEntity5))
            `when`(api.putHabit(anyString(), any())).thenReturn(habitUidJson4, habitUidJson5)
            `when`(habitDao.upsertHabit(habitEntity4WithUid)).thenReturn(Unit)
            `when`(habitDao.upsertHabit(habitEntity5WithUid)).thenReturn(Unit)

            // When
            habitRepository.putOfflineHabitList()

            // Then
            verify(api, times(1)).getHabitList(anyString())
            verify(habitDao, times(1)).getAllHabits()
            verify(api, times(2)).putHabit(anyString(), any())
            verify(habitDao, times(1)).upsertHabit(habitEntity4WithUid)
            verify(habitDao, times(1)).upsertHabit(habitEntity5WithUid)
        }

    @Test
    fun `putOfflineHabitList shouldn't save habits into api when no internet`() =
        runTest {
            // When
            doAnswer { throw Exception() }
                .`when`(api).getHabitList(anyString())

            // When
            habitRepository.putOfflineHabitList()

            // Then
            verify(api, times(3)).getHabitList(anyString())
        }

    @Test
    fun `putOfflineHabitList shouldn't save habits into api when no only local save habits`() =
        runTest {
            // When
            val getHabitJson1 = getMockGetHabitJson(uid = "uid1")
            val getHabitJson2 = getMockGetHabitJson(uid = "uid2")
            val habitEntity1 = getMockHabitEntity(uid = "uid1")
            val habitEntity2 = getMockHabitEntity(uid = "uid2")

            `when`(api.getHabitList(anyString()))
                .thenReturn(listOf(getHabitJson1, getHabitJson2))
            `when`(habitDao.getAllHabits())
                .thenReturn(listOf(habitEntity1, habitEntity2))

            // When
            habitRepository.putOfflineHabitList()

            // Then
            verify(api, times(1)).getHabitList(anyString())
            verify(habitDao, times(1)).getAllHabits()
        }

    @Test
    fun `postOfflineHabitList should send doneDates to api`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(uid = "uid1", doneDates = listOf(
                19610L, // 10/9/2023
                19613L, // 13/9/2023
            ))
            val habitEntity2 = getMockHabitEntity(uid = "uid2", doneDates = listOf(
                19611L, // 11/9/2023
                19614L, // 14/9/2023
            ))
            val habitEntity3 = getMockHabitEntity(uid = "uid3", doneDates = listOf(
                19611L, // 11/9/2023
                19615L, // 15/9/2023
            ))
            val getHabitJson1 = getMockGetHabitJson(uid = "uid1", doneDates = listOf(
                19610L, // 10/9/2023
                19613L, // 13/9/2023
            ))
            val getHabitJson2 = getMockGetHabitJson(uid = "uid2", doneDates = listOf(
                19611L, // 11/9/2023
            ))
            val getHabitJson3 = getMockGetHabitJson(uid = "uid3", doneDates = listOf(
                19615L, // 15/9/2023
            ))
            val getHabitJson4 = getMockGetHabitJson(uid = "uid4", doneDates = listOf(
                19616L, // 16/9/2023
            ))

            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1, habitEntity2, habitEntity3))
            `when`(api.getHabitList(anyString())).thenReturn(
                listOf(getHabitJson1, getHabitJson2, getHabitJson3, getHabitJson4)
            )
            `when`(api.postHabit(anyString(), any())).thenReturn(Unit)

            // When
            habitRepository.postOfflineHabitList()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(api, times(1)).getHabitList(anyString())
            verify(api, times(2)).postHabit(anyString(), any())
        }

    @Test
    fun `postOfflineHabitList shouldn't send doneDates to api when on internet`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(uid = "uid1", doneDates = listOf(
                19610L, // 10/9/2023
                19613L, // 13/9/2023
            ))

            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1))
            doAnswer { throw Exception() }
                .`when`(api).getHabitList(anyString())

            // When
            habitRepository.postOfflineHabitList()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(api, times(3)).getHabitList(anyString())
        }

    @Test
    fun `postOfflineHabitList shouldn't send doneDates to api when doneDates are equal`() =
        runTest {
            // Given
            val habitEntity1 = getMockHabitEntity(uid = "uid1", doneDates = listOf(
                19610L, // 10/9/2023
                19613L, // 13/9/2023
            ))
            val habitEntity2 = getMockHabitEntity(uid = "uid2", doneDates = listOf(
                19612L, // 12/9/2023
            ))
            val getHabitJson1 = getMockGetHabitJson(uid = "uid1", doneDates = listOf(
                19610L, // 10/9/2023
                19613L, // 13/9/2023
            ))
            val getHabitJson2 = getMockGetHabitJson(uid = "uid2", doneDates = listOf(
                19612L, // 12/9/2023
            ))

            `when`(habitDao.getAllHabits()).thenReturn(listOf(habitEntity1, habitEntity2))
            `when`(api.getHabitList(anyString())).thenReturn(listOf(getHabitJson1, getHabitJson2))

            // When
            habitRepository.postOfflineHabitList()

            // Then
            verify(habitDao, times(1)).getAllHabits()
            verify(api, times(1)).getHabitList(anyString())
        }


    private fun getMockHabitUidJson(uid: String): HabitUidJson {
        return HabitUidJson(
            uid = uid
        )
    }

    private fun getMockGetHabitJson(
        uid: String,
        doneDates: List<Long> = listOf(19610L), // 10/9/2023
    ): GetHabitJson {
        return GetHabitJson(
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = 0,
            priority = 0,
            type = 0,
            doneDates = doneDates,
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

    private fun getMockHabitEntity(
        id: String = "id",
        uid: String?,
        doneDates: List<Long> = listOf(19610L), // 10/9/2023
        deleted: Boolean = false
    ): HabitEntity {
        return HabitEntity(
            id = id,
            uid = uid,
            title = "title",
            description = "description",
            creationDate = 144424L,
            color = HabitColor.PINK,
            priority = HabitPriority.LOW,
            type = HabitType.GOOD,
            doneDates = doneDates,
            count = HabitCount.WEEK,
            frequency = 3,
            deleted = deleted
        )
    }

    private fun getMockHabitId(): String {
        return "id"
    }
}