package com.example.newapppp.ui.habit_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.newapppp.data.Habit
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.ui.habit_list.habit_adapter.HabitDiffUtilCallback
import com.example.newapppp.ui.habit_list.habit_adapter.HabitViewHolder

class HabitListAdapter(
    private val habitPriorityMapper: HabitPriorityMapper,
    private val openHabitClick: (String) -> Unit
) : ListAdapter<Habit, HabitViewHolder>(HabitDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding, openHabitClick, habitPriorityMapper)
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