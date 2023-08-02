package com.example.newapppp.ui.home

import android.annotation.SuppressLint
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
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.ui.habitadapter.HabitListAdapter
import com.example.newapppp.ui.home.HomeSerializable.Companion.serialazible
import com.example.newapppp.ui.typeofhabits.ViewPagerFilterFragmentDirections
import com.example.newapppp.ui.typeofhabits.ViewPagerViewModel


class HomeFragment : Fragment(R.layout.home_fragment) {

    companion object {
        fun newInstance(habitType: Type): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE_KEY, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val vpViewModel: ViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HabitListAdapter(::openHabitClick)
        binding.recycleViewHabit.adapter = adapter

        val habitType = arguments?.serialazible(HABIT_TYPE_KEY, Type::class.java)
        habitType?.let { type ->
            vpViewModel.habitFilter(type).observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
        swipeToDelete(adapter)
    }

    private fun openHabitClick(habit: Habit) {
        val action = ViewPagerFilterFragmentDirections.navPagerToRedactorFragment(habit)
        findNavController().navigate(action)
    }

    private fun swipeToDelete(adapter: HabitListAdapter) {
        val itemTouchHelper = ItemTouchHelper(object: SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.getHabitAtPosition(position)
                habit?.let {
                    adapter.deleteHabitByPosition(position)
                    vpViewModel.deleteById(it.id)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}