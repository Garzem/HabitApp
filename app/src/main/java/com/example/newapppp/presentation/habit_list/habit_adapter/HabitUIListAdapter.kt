package com.example.newapppp.presentation.habit_list.habit_adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.domain.model.HabitUI
import com.example.newapppp.presentation.habit_list.mapper.HabitColorMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitCountMapper
import com.example.newapppp.presentation.habit_list.mapper.HabitPriorityMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HabitUIListAdapter @Inject constructor(
    @ApplicationContext private val context: Context)
    : ListAdapter<HabitUI, HabitUIViewHolder>(HabitDiffUtilCallback()) {

    @Inject
    lateinit var habitPriorityMapper: HabitPriorityMapper

    @Inject
    lateinit var habitColorMapper: HabitColorMapper

    @Inject
    lateinit var habitCountMapper: HabitCountMapper


    var openHabitClick: (String) -> Unit = {}

    var openDoneDatesDialog: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitUIViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitUIViewHolder(
            binding = binding,
            openHabitClick = openHabitClick,
            openDoneDatesDialog = openDoneDatesDialog,
            habitPriorityMapper = habitPriorityMapper,
            habitCountMapper = habitCountMapper,
            habitColorMapper = habitColorMapper,
            context = context
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