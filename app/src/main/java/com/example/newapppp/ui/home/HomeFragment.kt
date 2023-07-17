package com.example.newapppp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.newapppp.databinding.HomeFragmentBinding
import androidx.navigation.fragment.findNavController
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.ui.habitadapter.HabitListAdapter
import com.example.newapppp.ui.typeofhabits.ViewPagerFilterFragmentDirections
import com.example.newapppp.ui.typeofhabits.ViewPagerViewModel


class HomeFragment : Fragment() {

    companion object {
        //создаётся в качестве ключа сохранения в Bundle() для habit_type
        const val HABIT_TYPE = "habit_type"
        //создаёт новый экземпляр текущего фрагмента
        fun newInstance(habitType: Type): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            //помещаем объекты, которые можно сериализовать
            bundle.putSerializable(HABIT_TYPE, habitType)
            //передаём данные в специально предназначенное для них место
            fragment.arguments = bundle
            return fragment
        }
    }

    private val vpViewModel: ViewPagerViewModel by activityViewModels()

    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater)
        return _binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = HomeFragmentBinding.bind(view)
        val fab = binding.fab
        fab.setOnClickListener {
            navigateToRedactorFragment()
        }
        //получение данных о привычке
        val adapter = HabitListAdapter(requireContext(), ::openHabitClick)
        binding.recycleViewHabit.adapter = adapter
        //делаем observe, чтобы установить данные в адаптер
        vpViewModel.habitList.observe(viewLifecycleOwner) { habitList ->
            adapter.submitList(habitList)
        }

    }


    private fun navigateToRedactorFragment() {
        val action = ViewPagerFilterFragmentDirections.navPagerToRedactorFragment(null)
        findNavController().navigate(action)
    }

    private fun openHabitClick(habit: Habit) {
        val action = ViewPagerFilterFragmentDirections.navPagerToRedactorFragment(habit)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}