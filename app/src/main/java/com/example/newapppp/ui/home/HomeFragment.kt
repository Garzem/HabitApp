package com.example.newapppp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.habit_local.HabitType
import com.example.newapppp.databinding.HomeFragmentBinding
import com.example.newapppp.ui.filter.BottomFilterFragment
import com.example.newapppp.ui.habit_list.HabitListFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.home_fragment) {
    private val homeViewModel: HomeFragmentViewModel by viewModels()
    private val binding by viewBinding(HomeFragmentBinding::bind)

    private val habitListFragments = listOf(
        HabitListFragment.newInstance(HabitType.GOOD),
        HabitListFragment.newInstance(HabitType.BAD)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pagerChooseHabit.adapter = HomeFragmentAdapter(
            this,
            habitListFragments
        )

        tabMediator()
        makeNewHabit()
        openFilter()
        deleteAllHabits()
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
        binding.fab.setOnClickListener {
            val selectedTabPosition = binding.tabLayout.selectedTabPosition
            val habitType = if (selectedTabPosition == 0) HabitType.GOOD else HabitType.BAD

            val action = HomeFragmentDirections.homeFragmentToRedactorFragment(null, habitType)
            findNavController().navigate(action)
        }
    }

    private fun openFilter() {
            binding.filterFab.setOnClickListener {
                val selectedTabPosition = binding.tabLayout.selectedTabPosition
                BottomFilterFragment().show(
                    habitListFragments[selectedTabPosition].childFragmentManager,
                    "BottomFilterFragment"
                )
        }
    }

    private fun deleteAllHabits() {
        binding.deleteFab.setOnClickListener{
            homeViewModel.deleteAllHabits()
        }
    }

    fun setupFilterBadge(isFilterApplied: Boolean) {
        binding.filterFabBadge.isVisible = isFilterApplied
    }
}