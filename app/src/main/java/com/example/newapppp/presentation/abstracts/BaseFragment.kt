package com.example.newapppp.presentation.abstracts

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.newapppp.presentation.util.collectWithLifecycle
import kotlinx.coroutines.Job

abstract class BaseFragment<State: BaseState, Event: BaseEvent>(@LayoutRes contentLayoutId: Int)
    : Fragment(contentLayoutId) {

        abstract val viewModel: BaseViewModel<State, Event>

        private var observerEventJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectWithLifecycle(viewModel.state) { state ->
            handleState(state)
        }

        observerEventJob?.cancel()
        observerEventJob = collectWithLifecycle(viewModel.event) { event ->
            event?.let {
                handleEvent(event)
                viewModel.consumeEvents()
            }
        }
    }

    abstract fun handleEvent(event: Event)

    abstract fun handleState(state: State)
}