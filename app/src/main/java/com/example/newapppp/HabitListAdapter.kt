package com.example.habit_create

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.ui.data.Habit


// объединяет список с Model и указывает, что будет работать со вложенным классом ViewHolder
class HabitListAdapter(

    // принимает аргумент и (unit) действие не возвращает значение
    private val openHabitChange: (index: Int) -> Unit
) : RecyclerView.Adapter<HabitListAdapter.ViewHolder>() {

    // инициализирует пустой список для отображения в RecyclerView
    private var habits: MutableList<Habit> = mutableListOf()

    // привязывает макет item_layout к элементам списка
    class ViewHolder(val itemBinding: ItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)
    // создаёт новый view, т.е то, что должно выводиться на экран

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // описывается получение элемента списка
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        // возвращает уже созданную view
        return ViewHolder(binding)
    }

    // возвращает количество элементов, в данном случае кол-во параметров
    override fun getItemCount(): Int {
        return habits.size
    }

    //привязывает данные
    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val currentItem = habits[index]


        // устанавливает слушатель на элемент списка
        holder.itemView.setOnClickListener {
            openHabitChange(index)
        }
    }

    // принимает список объектов и обновляет их в adapter. благодаря функциям recyclerview
    // идёт обновление одного view, а не всего кода, поэтому не нужно это указывать в коде
    @SuppressLint("NotifyDataSetChanged")
    fun setList(newHabit: List<Habit>) {
        habits = newHabit as MutableList<Habit>
        notifyDataSetChanged()
        // getHabit
    }
}