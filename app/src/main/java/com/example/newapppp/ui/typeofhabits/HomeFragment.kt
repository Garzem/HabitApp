package com.example.newapppp.ui.typeofhabits

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants.HABIT_KEY
import com.example.newapppp.data.Constants.HABIT_ID_FROM_REDACTOR_KEY
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.databinding.ViewPagerFragmentBinding
import com.example.newapppp.ui.habitlist.HabitListFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.view_pager_fragment) {
    private val homeViewModel: HomeFragmentViewModel by viewModels()
    private val binding by viewBinding(ViewPagerFragmentBinding::bind)
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrUpdateNewHabit()
        deleteHabit()
        val adapter = HomeFragmentAdapter(
            this,
            listOf<Fragment>(
                HabitListFragment.newInstance(Type.GOOD),
                HabitListFragment.newInstance(Type.BAD)
            )
        )

        viewPager = binding.pagerChooseHabit
        viewPager.adapter = adapter
        tabMediator()

        val fab = binding.fab
        fab.setOnClickListener {
            navigateToRedactorFragment()
        }
    }

    private fun setupOrUpdateNewHabit() {
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<Habit>(HABIT_KEY).observe(viewLifecycleOwner)
            { updatedHabit ->
                homeViewModel.updateHabitList(updatedHabit)
                entry.savedStateHandle.remove<Habit>(HABIT_KEY)
            }
        }
    }

    private fun deleteHabit() {
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<String>(HABIT_ID_FROM_REDACTOR_KEY).observe(viewLifecycleOwner)
            {habitId ->
                homeViewModel.deleteById(habitId)
                entry.savedStateHandle.remove<String>(HABIT_ID_FROM_REDACTOR_KEY)
            }
        }
    }

    private fun navigateToRedactorFragment() {
        val action = ViewPagerFilterFragmentDirections.navPagerToRedactorFragment(null)
        findNavController().navigate(action)
    }

    private fun tabMediator() {
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.good_habit)
            else
                getString(R.string.bad_habit)
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}