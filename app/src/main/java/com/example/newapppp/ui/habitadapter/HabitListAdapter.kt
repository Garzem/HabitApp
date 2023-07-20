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

    //прикрепляем к родительскому xml
    //до {} сигнатура класса, дальше тело
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        // описывается получение элемента списка
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding, context, openHabitClick)
    }

    //привязывает данные
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun deleteHabitByPosition(habitPosition: Int) {
        val currentList = currentList.toMutableList()
        currentList.removeAt(habitPosition)
        submitList(currentList)
    }

    fun getHabitAtPosition(position: Int): Habit? {
        // Проверяем, что позиция находится в допустимом диапазоне индексов currentList
        return if (position in currentList.indices) {
            // Если позиция в допустимом диапазоне, то возвращаем элемент с этой позиции
            // из списка currentList
            currentList[position]
        } else {
            null
        }
    }
}