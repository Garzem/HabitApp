package com.example.newapppp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.HabitType
import com.example.newapppp.databinding.HomeFragmentBinding
import com.example.newapppp.ui.habit_list.HabitListFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.home_fragment) {
//    private val homeViewModel: HomeFragmentViewModel by viewModels()
    private val binding by viewBinding(HomeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomeFragmentAdapter(
            this,
            listOf<Fragment>(
                HabitListFragment.newInstance(HabitType.GOOD),
                HabitListFragment.newInstance(HabitType.BAD)
            )
        )

        binding.pagerChooseHabit.adapter = adapter
        tabMediator()
        makeNewHabit()
        openFilter()
    }

    private fun tabMediator() {
        val tabLayout = binding.tabLayout
        val pagerChooseHabit = binding.pagerChooseHabit
        TabLayoutMediator(tabLayout, pagerChooseHabit) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.good_habit)
            else
                getString(R.string.bad_habit)
            pagerChooseHabit.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun makeNewHabit() {
        val fab = binding.fab
        fab.setOnClickListener {
            val selectedTabPosition = binding.tabLayout.selectedTabPosition
            val habitType = if (selectedTabPosition == 0) HabitType.GOOD else HabitType.BAD

            val action = HomeFragmentDirections.homeFragmentToRedactorFragment(null, habitType)
            findNavController().navigate(action)
        }
    }

    private fun openFilter() {
        val filterFab = binding.filterFab
        filterFab.setOnClickListener {
            val selectedTabPosition = binding.tabLayout.selectedTabPosition
            val habitType = if (selectedTabPosition == 0) HabitType.GOOD else HabitType.BAD

            val action = HomeFragmentDirections.homeFragmentToFilterDialog(habitType)
            findNavController().navigate(action)
        }
    }
}