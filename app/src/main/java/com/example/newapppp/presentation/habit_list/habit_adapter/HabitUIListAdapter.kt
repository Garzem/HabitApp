package com.example.newapppp.presentation.habit_list.habit_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.HabitColorMapper
import com.example.newapppp.presentation.habit_list.HabitPriorityMapper
import javax.inject.Inject

class HabitUIListAdapter @Inject constructor() : ListAdapter<HabitUI, HabitUIViewHolder>(HabitDiffUtilCallback()) {

    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    var openHabitClick: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitUIViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitUIViewHolder(
            binding = binding,
            openHabitClick = openHabitClick,
            habitPriorityMapper = habitPriorityMapper,
            habitColorMapper = habitColorMapper
        )
    }

    override fun onBindViewHolder(holder: HabitUIViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun deleteHabitByPosition(habitPosition: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(habitPosition)
        submitList(currentList)
    }

    fun getHabitAtPosition(position: Int): HabitUI? {
        return if (position in currentList.indices) {
            currentList[position]
        } else {
            null
        }
    }
}