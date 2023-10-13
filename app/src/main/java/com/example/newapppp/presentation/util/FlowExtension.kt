package com.example.newapppp.presentation.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Fragment.collectWithLifecycle(
    flow: Flow<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
): Job {
    return lifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle, lifecycleState).collect {
            block(it)
        }
    }
}
