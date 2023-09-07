package com.example.newapppp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapppp.data.repository.HabitRepositoryImpl
import com.example.newapppp.domain.usecase.DeleteAllHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val deleteAllHabitsUseCase: DeleteAllHabitsUseCase
    ) : ViewModel() {

    fun deleteAllHabits() {
        viewModelScope.launch {
            try {
                deleteAllHabitsUseCase()
            } catch (e: Exception) {
                Log.d("wrong", "$e")
            }
        }
    }
}
