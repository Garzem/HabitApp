package com.example.newapppp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.newapppp.databinding.HomeFragmentBinding
import androidx.navigation.fragment.findNavController
import com.example.newapppp.data.Habit
import com.example.newapppp.data.HabitList
import com.example.newapppp.data.Type
import com.example.newapppp.ui.habitadapter.HabitListAdapter


class HomeFragment : Fragment() {

    companion object {
        //создаётся в качестве ключа сохранения в Bundle() для habit_type
        const val HABIT_TYPE = "habit_type"
        //создаёт новый экземпляр текущего фрагмента
        fun newInstance(habitType: Type): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            //помещаем объекты, которые можно сериализовать
            bundle.putSerializable(HABIT_TYPE,habitType)
            //передаём данные в специально предназначенное для них место
            fragment.arguments = bundle
            return fragment
        }
    }

    private val homeViewModel: HomeViewModel by viewModels()

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
        //устанавлявает наблюдатель для habit, он будет активен в течении жизненного цикла фрагмента
        homeViewModel.habit.observe(viewLifecycleOwner) {
            //при изменении habit выполняется этот код по его обновлению
            adapter.updateData(HabitList.getHabits())
            adapter.notifyDataSetChanged()
        }
    }


    private fun navigateToRedactorFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToRedactorFragment()
        findNavController().navigate(action)
    }

    private fun openHabitClick(habit: Habit) {
        homeViewModel.setHabit(habit)
        val action = HomeFragmentDirections.actionHomeFragmentToRedactorFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}