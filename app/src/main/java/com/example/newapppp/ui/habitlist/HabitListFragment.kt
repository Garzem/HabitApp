package com.example.newapppp.ui.habitlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newapppp.databinding.HomeFragmentBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants.HABIT_TYPE_KEY
import com.example.newapppp.data.HabitType
import com.example.newapppp.database.HabitEntity
import com.example.newapppp.ui.habitlist.HabitListSerializable.Companion.serializable
import com.example.newapppp.ui.typeofhabits.HomeFragmentDirections
import com.example.newapppp.ui.typeofhabits.HomeFragmentViewModel


class HabitListFragment : Fragment(R.layout.home_fragment) {

    companion object {
        fun newInstance(habitType: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE_KEY, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val homeViewModel: HomeFragmentViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitListAdapter(::openHabitClick)
        binding.recycleViewHabit.adapter = adapter

        val habitType = arguments?.serializable(HABIT_TYPE_KEY, HabitType::class.java)
        habitType?.let { type ->
            homeViewModel.habitFilter(type).observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
        swipeToDelete(adapter)
    }

    private fun openHabitClick(habit: HabitEntity) {
        val action = HomeFragmentDirections.navPagerToRedactorFragment(habit)
        findNavController().navigate(action)
    }

    private fun swipeToDelete(adapter: HabitListAdapter) {
        val itemTouchHelper = ItemTouchHelper(object: SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.getHabitAtPosition(position)
                habit?.let {
                    adapter.deleteHabitByPosition(position)
                    homeViewModel.deleteById(it.id)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}