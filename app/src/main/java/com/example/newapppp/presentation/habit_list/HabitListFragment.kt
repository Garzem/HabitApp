package com.example.newapppp.presentation.habit_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.domain.Constants.HABIT_TYPE_KEY
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.databinding.HabitListFragmentBinding

import com.example.newapppp.domain.extension.collectWithLifecycle
import com.example.newapppp.domain.extension.serializable
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.presentation.habit_list.habit_adapter.HabitUIListAdapter
import com.example.newapppp.presentation.home.HomeFragment
import com.example.newapppp.presentation.home.HomeFragmentDirections
import com.example.newapppp.presentation.util.HabitUIConverter
import com.example.newapppp.presentation.util.TimeConverter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HabitListFragment : Fragment(R.layout.habit_list_fragment) {

    companion object {
        fun newInstance(habitType: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE_KEY, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val habitViewModel: HabitListViewModel by viewModels()
    private val binding by viewBinding(HabitListFragmentBinding::bind)
    private val habitType: HabitType? by lazy {
        arguments?.serializable(HABIT_TYPE_KEY, HabitType::class.java)
    }

    @Inject
    lateinit var adapter: HabitUIListAdapter

    @Inject
    lateinit var habitUIConverter: HabitUIConverter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.openHabitClick = ::openHabitClick
        adapter
        binding.recycleViewHabit.adapter = adapter

        habitType?.let {
            habitViewModel.fetchHabitList(it)
        }
        collectWithLifecycle(habitViewModel.habitState) { state ->
            when (state) {
                is HabitState.Success -> {
                    binding.progressBar.isVisible = false
                    adapter.submitList(state.filteredHabits.map { habit ->
                        habitUIConverter.toHabitUI(habit)
                    })
                }

                is HabitState.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
        filterObserver()
        swipeToDelete()
    }

    private fun openHabitClick(habitId: String) {
        val action = HomeFragmentDirections.homeFragmentToRedactorFragment(habitId, null)
        findNavController().navigate(action)
    }

    private fun openDoneDatesDialog() {

    }

    private fun filterObserver() {
        collectWithLifecycle(habitViewModel.habitState) { state ->
            (requireParentFragment() as HomeFragment).setupFilterBadge(
                state is HabitState.Success && state.filter.isFilterApplied
            )
        }
    }

    private fun swipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habitUI = adapter.getHabitAtPosition(position) ?: return
                habitUI.apply {
                    adapter.deleteHabitByPosition(position)
                    habitViewModel.deleteHabit(habitUIConverter.toHabit(habitUI))
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }

    fun deleteAllHabit() {
        habitViewModel.deleteAllHabits()
    }
}