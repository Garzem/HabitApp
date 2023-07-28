package com.example.newapppp.ui.home

import android.annotation.SuppressLint
import android.os.BaseBundle
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
import com.example.newapppp.data.Habit
import com.example.newapppp.data.Type
import com.example.newapppp.ui.habitadapter.HabitListAdapter
import com.example.newapppp.ui.home.HomeSerializable.Companion.serialazible
import com.example.newapppp.ui.typeofhabits.ViewPagerFilterFragmentDirections
import com.example.newapppp.ui.typeofhabits.ViewPagerViewModel


class HomeFragment : Fragment(R.layout.home_fragment) {

    companion object {
        //создаётся в качестве ключа сохранения в Bundle() для habit_type
        private const val HABIT_TYPE_KEY = "habit_type"
        //создаёт новый экземпляр текущего фрагмента
        fun newInstance(habitType: Type): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            //помещаем объекты, которые можно сериализовать
            bundle.putSerializable(HABIT_TYPE_KEY, habitType)
            //передаём данные в специально предназначенное для них место
            fragment.arguments = bundle
            return fragment
        }
    }

    private val vpViewModel: ViewPagerViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val binding by viewBinding(HomeFragmentBinding::bind)

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //получение данных о привычке
        val adapter = HabitListAdapter(requireContext(), ::openHabitClick)
        binding.recycleViewHabit.adapter = adapter
        //делаем observe, чтобы установить данные в адаптер
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
                //возвращает позицию элемента в адаптере
                val position = viewHolder.adapterPosition
                val habit = adapter.getHabitAtPosition(position)
                habit?.let {
                    adapter.deleteHabitByPosition(position)
                    vpViewModel.deleteById(it.id)
                }
            }
        })
        //связывает ItemTouchHelper с RecyclerView,
        //чтобы обработчик смахивания (swipe) элементов сработал внутри RecyclerView.
        itemTouchHelper.attachToRecyclerView(binding.recycleViewHabit)
    }
}