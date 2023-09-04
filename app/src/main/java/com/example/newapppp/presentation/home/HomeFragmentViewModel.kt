package com.example.newapppp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.habit_repository.HabitRepository
import kotlinx.coroutines.launch
import java.lang.Exception


class HomeFragmentViewModel : ViewModel() {

    fun deleteAllHabits() {
        viewModelScope.launch {
            try {
                HabitRepository().deleteAllHabits()
            } catch (e: Exception) {
                Log.d("wrong", "$e")
            }
        }
    }
}
