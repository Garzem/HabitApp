package com.example.newapppp.presentation.habit_list.habit_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.newapppp.domain.model.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class HabitListAdapter @Inject constructor() : ListAdapter<Habit, HabitViewHolder>(HabitDiffUtilCallback()) {

    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    var openHabitClick: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitViewHolder(
            binding = binding,
            openHabitClick = openHabitClick,
            habitPriorityMapper = habitPriorityMapper,
            habitColorMapper = habitColorMapper
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun deleteHabitByPosition(habitPosition: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(habitPosition)
        submitList(currentList)
    }

    fun getHabitAtPosition(position: Int): Habit? {
        return if (position in currentList.indices) {
            currentList[position]
        } else {
            null
        }
    }
}