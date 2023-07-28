package com.example.newapppp.ui.typeofhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.databinding.ActivityMainBinding
import com.example.newapppp.databinding.HomeFragmentBinding
import com.example.newapppp.databinding.ViewPagerFragmentBinding
import com.example.newapppp.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFilterFragment : Fragment(R.layout.view_pager_fragment) {
    private val vpViewModel: ViewPagerViewModel by viewModels()
    private val binding by viewBinding(ViewPagerFragmentBinding::bind)
    private lateinit var viewPager: ViewPager2

    //вся настройка ui и вешание listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrUpdateNewHabit()
        deleteHabit()
        val adapter = ViewPagerFilterAdapter(
            //передаёт фрагмент в качестве контекста для адаптера
            this,
            listOf<Fragment>(
                HomeFragment.newInstance(Type.GOOD),
                HomeFragment.newInstance(Type.BAD)
            )
        )
        //??как я могу вызвать arraylist, если в adapter у меня в конструкторе стоит list?

        //В <> указывается тип view
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
            entry.savedStateHandle.getLiveData<Habit>("habit").observe(viewLifecycleOwner)
            { updatedHabit ->
                vpViewModel.updateHabitList(updatedHabit)
                entry.savedStateHandle.remove<Habit>("habit")
            }
        }
    }

    private fun deleteHabit() {
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<String>("habitIdFromRedactor").observe(viewLifecycleOwner)
            {habitId ->
                vpViewModel.deleteById(habitId)
                entry.savedStateHandle.remove<String>("habitIdFromRedactor")
            }
        }
    }

    private fun navigateToRedactorFragment() {
        val action = ViewPagerFilterFragmentDirections.navPagerToRedactorFragment(null)
        findNavController().navigate(action)
    }

    private fun tabMediator() {
        val tabLayout = binding.tabLayout
        //определяет как будут задаваться вкладки(табы)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0)
                getString(R.string.good_habit)
            else
                getString(R.string.bad_habit)
            //устанавливает позицию для таба и добавляет анимацию
            viewPager.setCurrentItem(tab.position, true)
            //привязывает к viewPager2 и активирует функциональность TabLayoutMediator
        }.attach()
    }
}