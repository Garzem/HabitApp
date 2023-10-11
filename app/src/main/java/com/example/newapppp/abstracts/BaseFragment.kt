package com.example.newapppp.abstracts

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.newapppp.presentation.util.collectWithLifecycle

abstract class BaseFragment<State: BaseState, Event: BaseEvent>(@LayoutRes contentLayoutId: Int)
    : Fragment(contentLayoutId) {

        abstract val viewModel: BaseViewModel<State, Event>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectWithLifecycle(viewModel.state) {}

        collectWithLifecycle(viewModel.event) { event ->
            event?.let {
                handleEvent(event)
                viewModel.consumeEvents()
            }
        }
    }

    abstract fun handleEvent(event: Event)
}