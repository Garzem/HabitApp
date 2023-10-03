package com.example.newapppp.presentation.filter

import androidx.lifecycle.Observer
import com.example.newapppp.domain.model.HabitPriority
import com.example.newapppp.domain.repository.FilterRepository
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class BottomFilterViewModelTest {

    private val habitPriorityMapper = mock<HabitPriorityMapper>()
    private val filterRepository = mock<FilterRepository>()

    private lateinit var viewModel: BottomFilterViewModel

    @Before
    fun setUp() {
        viewModel = BottomFilterViewModel(habitPriorityMapper, filterRepository)
    }

    @After
    fun afterEach() {
        Mockito.reset(habitPriorityMapper)
        Mockito.reset(filterRepository)
    }

    @Test
    fun `onFilterClicked emits showErrorToast if title is blank and priority is CHOOSE`() {
        // Given
        val title = ""
        val priority = HabitPriority.CHOOSE
        val observer = mock<Observer<Unit>>()

        // When
        viewModel.showErrorToast.observeForever(observer)
        viewModel.onFilterClicked(title)

        // Then
        verify(observer).onChanged(Unit)
    }

}