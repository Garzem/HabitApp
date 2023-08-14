package com.example.newapppp.ui.habit_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants.HABIT_TYPE_KEY
import com.example.newapppp.data.HabitType
import com.example.newapppp.databinding.HabitListFragmentBinding

import com.example.newapppp.extension.collectWithLifecycle
import com.example.newapppp.extension.serializable
import com.example.newapppp.ui.home.HomeFragment
import com.example.newapppp.ui.home.HomeFragmentDirections


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

    private val viewModel: HabitListViewModel by viewModels()
    private val binding by viewBinding(HabitListFragmentBinding::bind)
    private val habitType: HabitType? by lazy {
        arguments?.serializable(HABIT_TYPE_KEY, HabitType::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitListAdapter(HabitPriorityMapper(requireContext()), ::openHabitClick)
        binding.recycleViewHabit.adapter = adapter
        habitType?.let {
            viewModel.setHabitType(it)
        }
        collectWithLifecycle(viewModel.habitList) { habits ->
            adapter.submitList(habits)
        }
        filterObserver()
        swipeToDelete(adapter)
    }

    private fun openHabitClick(habitId: String) {
        val action = HomeFragmentDirections.homeFragmentToRedactorFragment(habitId, null)
        findNavController().navigate(action)
    }

    private fun filterObserver () {
        collectWithLifecycle(viewModel.isFilterApplied) { result ->
            (requireParentFragment() as HomeFragment).setupFilterBadge(result)
        }
    }

    private fun swipeToDelete(adapter: HabitListAdapter) {
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.getHabitAtPosition(position) ?: return
                habit.apply {
                    adapter.deleteHabitByPosition(position)
                    id.let { viewModel.deleteHabit(it) }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}