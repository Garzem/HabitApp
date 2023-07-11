package com.example.newapppp.ui.habitadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.data.Habit


// объединяет список с Model и указывает, что будет работать со вложенным классом ViewHolder
class HabitListAdapter(
    //() указывают на то, что это callback(функция обратного вызова)
    private val context: Context,
    private val openHabitClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitViewHolder>(HabitDiffUtilCallback()) {

    private var habits: MutableList<Habit> = mutableListOf()

    //прикрепляем к родительскому xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        // описывается получение элемента списка
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding, openHabitClick)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    //привязывает данные
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        //связывает данные с конкретным элементом списка
        //передаётся 1 habit в список habits
        holder.bind(context, habits[position])
    }
}