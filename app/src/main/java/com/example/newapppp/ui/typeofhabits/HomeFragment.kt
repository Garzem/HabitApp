package com.example.newapppp.ui.typeofhabits

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.HabitType
import com.example.newapppp.databinding.ViewPagerFragmentBinding
import com.example.newapppp.habitrepository.HabitRepository
import com.example.newapppp.ui.habitlist.HabitListFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.view_pager_fragment) {
    private val homeViewModel: HomeFragmentViewModel by viewModels()
    private val binding by viewBinding(ViewPagerFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrUpdateNewHabit()
//        deleteHabit()
        val adapter = HomeFragmentAdapter(
            this,
            listOf<Fragment>(
                HabitListFragment.newInstance(HabitType.GOOD),
                HabitListFragment.newInstance(HabitType.BAD)
            )
        )

        binding.pagerChooseHabit.adapter = adapter
        tabMediator()

        val fab = binding.fab
        fab.setOnClickListener {
            navigateToRedactorFragment()
        }
    }

//    private fun setupOrUpdateNewHabit() {
//        lifecycleScope.launch {
//            val goodHabits = homeViewModel.setupGoodHabits()
//            val badHabits = homeViewModel.setupBadHabits()
//
//            homeViewModel.updateHabitList(goodHabits)
//            homeViewModel.updateHabitList(badHabits)
//        }
//        findNavController().currentBackStackEntry?.let { entry ->
//            entry.savedStateHandle.getLiveData<HabitEntity>(HABIT_KEY).observe(viewLifecycleOwner)
//            { updatedHabit ->
//                homeViewModel.updateHabitList(updatedHabit)
//                entry.savedStateHandle.remove<HabitEntity>(HABIT_KEY)
//            }
//        }
//    }

//    private fun deleteHabit() {
//        findNavController().currentBackStackEntry?.let { entry ->
//            entry.savedStateHandle.getLiveData<String>(HABIT_ID_FROM_REDACTOR_KEY).observe(viewLifecycleOwner)
//            {habitId ->
//                homeViewModel.deleteById(habitId)
//                entry.savedStateHandle.remove<String>(HABIT_ID_FROM_REDACTOR_KEY)
//            }
//        }
//    }

    private fun navigateToRedactorFragment() {
        val action = HomeFragmentDirections.navPagerToRedactorFragment(null)
        findNavController().navigate(action)
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
}