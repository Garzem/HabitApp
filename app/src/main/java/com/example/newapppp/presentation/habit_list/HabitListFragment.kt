package com.example.newapppp.presentation.habit_list

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.databinding.HabitListFragmentBinding
import com.example.newapppp.domain.Constants.HABIT_TYPE_KEY
import com.example.newapppp.domain.extension.collectWithLifecycle
import com.example.newapppp.domain.extension.serializable
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.presentation.habit_list.habit_adapter.HabitUIListAdapter
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.presentation.home.HomeFragment
import com.example.newapppp.presentation.home.HomeFragmentDirections
import com.example.newapppp.presentation.util.HabitUIConverter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.openHabitClick = ::openHabitClick
        adapter.openDoneDatesDialog = ::markDoneDay
        binding.recycleViewHabit.adapter = adapter

        habitType?.let {
            habitViewModel.getAndRefreshHabitList(it)
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
        observeProcessCheck()
        swipeToDelete()
    }

    private fun openHabitClick(habitId: String) {
        val action = HomeFragmentDirections.homeFragmentToRedactorFragment(habitId, null)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun markDoneDay(habitId: String) {
        val selectedDate = LocalDate.now().toEpochDay()
        habitViewModel.saveDoneDatesForHabit(habitId, selectedDate)
    }

    private fun filterObserver() {
        collectWithLifecycle(habitViewModel.habitState) { state ->
            (requireParentFragment() as HomeFragment).setupFilterBadge(
                state is HabitState.Success && state.filter.isFilterApplied
            )
        }
    }

    private fun observeProcessCheck() {
        habitViewModel.apply {
            timesLeftForBadHabit.observe(viewLifecycleOwner) { remainingExecutions ->
                val message = getString(R.string.times_left_for_bad_habit, remainingExecutions)
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            stopDoingBadHabit.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.stop_doing_it,
                    Toast.LENGTH_SHORT
                ).show()
            }
            timesLeftForGoodHabit.observe(viewLifecycleOwner) { remainingExecutions ->
                val message = getString(R.string.times_left_for_good_habit, remainingExecutions)
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            metOrExceededGoodHabit.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.you_are_breathtaking,
                    Toast.LENGTH_SHORT
                ).show()
            }
            showError.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    R.string.something_went_wrong,
                    Toast.LENGTH_SHORT
                ).show()
            }
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
}