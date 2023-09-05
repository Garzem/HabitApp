package com.example.newapppp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.repository.HabitRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

@HiltViewModel
class HomeFragmentViewModel : ViewModel() {

    fun deleteAllHabits() {
        viewModelScope.launch {
            try {
                HabitRepositoryImpl().deleteAllHabits()
            } catch (e: Exception) {
                Log.d("wrong", "$e")
            }
        }
    }
}
