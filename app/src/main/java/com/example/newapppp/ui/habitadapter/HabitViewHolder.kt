package com.example.newapppp.ui.habitadapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.R
import com.example.newapppp.data.Habit
import com.example.newapppp.databinding.ItemLayoutBinding


//Является подклассом RecyclerView.ViewHolder
//??? как отличить подкласс от реализации интерфейса?
class HabitViewHolder(
    private val binding: ItemLayoutBinding,
    private val context: Context,
    private val openHabitClick: (Habit) -> Unit,
//указывает на корневое представление макета списка для ViewHolder
): RecyclerView.ViewHolder(binding.root) {

    //связывание данных элемента списка с его макетом
    fun bind(habit: Habit) {
        val shapeDrawable = ContextCompat.getDrawable(context, R.drawable.color_button_orange) as GradientDrawable
        val color = ContextCompat.getColor(context, habit.color.getColorRedId())
        shapeDrawable.setColor(color)
        binding.title.text = habit.title
        binding.description.text = habit.description
        binding.period.text = habit.period
        binding.colorMain.background = shapeDrawable
        binding.colorSupport.background = shapeDrawable
        binding.priority.text = habit.getPriorityName()
        binding.quantity.text = habit.quantity
        //обработчик нажатия на корневой элемент макета
        binding.root.setOnClickListener {
            openHabitClick(habit)
        }
    }
}
