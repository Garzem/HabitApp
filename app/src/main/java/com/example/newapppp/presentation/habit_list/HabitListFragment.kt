package com.example.newapppp.presentation.habit_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.abstracts.BaseFragment
import com.example.newapppp.abstracts.HabitListEvents
import com.example.newapppp.databinding.HabitListFragmentBinding
import com.example.newapppp.domain.Constants.HABIT_TYPE_KEY
import com.example.newapppp.presentation.util.collectWithLifecycle
import com.example.newapppp.domain.extension.serializable
import com.example.newapppp.domain.model.HabitType
import com.example.newapppp.domain.model.Message.TimesLeftForBadHabit
import com.example.newapppp.domain.model.Message.StopDoingBadHabit
import com.example.newapppp.domain.model.Message.TimesLeftForGoodHabit
import com.example.newapppp.domain.model.Message.MetOrExceededGoodHabit
import com.example.newapppp.domain.model.Message.Error
import com.example.newapppp.presentation.habit_list.habit_adapter.HabitUIListAdapter
import com.example.newapppp.presentation.habit_list.state.HabitState
import com.example.newapppp.presentation.home.HomeFragment
import com.example.newapppp.presentation.home.HomeFragmentDirections
import com.example.newapppp.presentation.util.HabitUIConverter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HabitListFragment : BaseFragment<HabitState, HabitListEvents>(R.layout.habit_list_fragment) {

    companion object {
        fun newInstance(habitType: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE_KEY, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val viewModel: HabitListViewModel by viewModels()
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
        adapter.openDoneDatesDialog = ::markDoneDay
        binding.recycleViewHabit.adapter = adapter

        habitType?.let {
            viewModel.getAndRefreshHabitList(it)
        }
        filterObserver()
        swipeToDelete()
    }

    override fun handleState(state: HabitState) {
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

    override fun handleEvent(event: HabitListEvents) {
        when (event) {
            is HabitListEvents.ShowMessage -> {
                when (val message = event.message) {
                    is TimesLeftForBadHabit -> {
                        val text = getString(R.string.times_left_for_bad_habit, message.remainingDays)
                        Toast.makeText(
                            requireContext(),
                            text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is StopDoingBadHabit -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.stop_doing_it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is TimesLeftForGoodHabit -> {
                        val text = getString(R.string.times_left_for_good_habit, message.remainingDays)
                        Toast.makeText(
                            requireContext(),
                            text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is MetOrExceededGoodHabit -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.you_are_breathtaking,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Error -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.something_went_wrong,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun openHabitClick(habitId: String) {
        val action = HomeFragmentDirections.homeFragmentToRedactorFragment(habitId, null)
        findNavController().navigate(action)
    }

    private fun markDoneDay(habitId: String) {
        viewModel.apply {
            val selectedDate = getCurrentDate()
            saveDoneDatesForHabit(habitId, selectedDate)
        }
    }

    private fun filterObserver() {
        collectWithLifecycle(viewModel.state) { state ->
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
                    viewModel.deleteHabit(habitUIConverter.toHabit(habitUI))
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}