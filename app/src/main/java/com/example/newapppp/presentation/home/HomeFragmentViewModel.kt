package com.example.newapppp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.domain.usecase.DeleteAllHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val deleteAllHabitsUseCase: DeleteAllHabitsUseCase
) : ViewModel() {

    fun deleteAllHabits() {
        viewModelScope.launch {
            deleteAllHabitsUseCase()
        }
    }
}
