package com.example.newapppp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.data.Habit


// объединяет список с Model и указывает, что будет работать со вложенным классом ViewHolder
class HabitListAdapter(
    private val openHabitChange: (index: Int) -> Unit
) : RecyclerView.Adapter<HabitListAdapter.HabitViewHolder>() {

    private var habits: MutableList<Habit> = mutableListOf()


    class ViewHolder(val itemBinding: ItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        // описывается получение элемента списка
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)

        return HabitViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    //привязывает данные
    override fun onBindViewHolder(holder: HabitViewHolder, index: Int) {

    }

    inner class HabitViewHolder(override val containerView: View) {

    }
}