package com.example.newapppp.data.remote.status

sealed interface StatusUiState {
    object Success: StatusUiState
    object Loading: StatusUiState
    object Error: StatusUiState
}