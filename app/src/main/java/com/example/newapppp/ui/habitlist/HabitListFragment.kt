package com.example.newapppp.ui.habitlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants.HABIT_TYPE_KEY
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitType
import com.example.newapppp.databinding.HabitListFragmentBinding
import com.example.newapppp.extension.FlowExtension
import com.example.newapppp.extension.HabitListSerializable.Companion.serializable
import com.example.newapppp.ui.home.HomeFragmentDirections
import kotlinx.coroutines.launch


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

    private val HLViewModel: HabitListViewModel by viewModels()
    private val binding by viewBinding(HabitListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitListAdapter(::openHabitClick)
        binding.recycleViewHabit.adapter = adapter

        val habitType = arguments?.serializable(HABIT_TYPE_KEY, HabitType::class.java)
//        habitType?.let { type ->
//            HLViewModel.habitFilter(type).observe(viewLifecycleOwner) {
//                adapter.submitList(it)
//            }
//        }
        habitType?.let { type ->
            getHabitByType(type)
            FlowExtension().apply {
                collectWithLifecycle(HLViewModel.habitList) { habits ->
                    adapter.submitList(habits)
                }
            }
        }
        swipeToDelete(adapter)
    }

    private fun openHabitClick(habit: Habit) {
        val action = HomeFragmentDirections.homeFragmentToRedactorFragment(habit)
        findNavController().navigate(action)
    }

    private fun getHabitByType(type: HabitType) {
        lifecycleScope.launch {
            val habitList = when (type) {
                HabitType.GOOD -> HLViewModel.setupGoodHabits()
                HabitType.BAD -> HLViewModel.setupBadHabits()
            }
            HLViewModel.updateHabitList(habitList)
        }
    }

    private fun swipeToDelete(adapter: HabitListAdapter) {
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.getHabitAtPosition(position) ?: return
                habit.apply {
                    adapter.deleteHabitByPosition(position)
                    id.let { HLViewModel.deleteHabit(it) }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}