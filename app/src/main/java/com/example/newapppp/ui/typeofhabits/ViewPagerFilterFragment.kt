package com.example.newapppp.ui.typeofhabits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.newapppp.R
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.databinding.ViewPagerFragmentBinding
import com.example.newapppp.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFilterFragment : Fragment() {
    private val vpViewModel: ViewPagerViewModel by activityViewModels()
    private var _binding: ViewPagerFragmentBinding? = null
    private lateinit var viewPager: ViewPager2
    private val binding get() = _binding!!

    //все действия с настройкой ui
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewPagerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    //вся настройка ui и вешание listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.let { entry ->
            entry.savedStateHandle.getLiveData<Habit>("habit").observe(viewLifecycleOwner)
            { habit ->
                vpViewModel.updateHabitList(habit)
                entry.savedStateHandle.remove<Habit>("habit")
            }
        }
        val adapter = ViewPagerFilterAdapter(
            //передаёт активность в качестве контекста для адаптера
            activity as AppCompatActivity,
            //список объектов, которые будут созданы внутри viewPager2
            // будут отображаться в порядке указаном в списке
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}