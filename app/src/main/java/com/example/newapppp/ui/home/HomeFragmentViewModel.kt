package com.example.newapppp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.habit_repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception


class HomeFragmentViewModel : ViewModel() {

    fun deleteAllHabits() {
        viewModelScope.launch {
            try {
                HabitRepository().deleteAllHabits()
            } catch (e: Exception) {
                //TODO
            }
        }
    }
}
