package com.example.newapppp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newapppp.databinding.ItemLayoutBinding
import com.example.newapppp.data.Habit
import com.example.newapppp.databinding.RedactorFragmentBinding


// объединяет список с Model и указывает, что будет работать со вложенным классом ViewHolder
class HabitListAdapter(
    //() указывают на то, что это callback(функция обратного вызова)
    private val openHabitClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitListAdapter.HabitViewHolder>() {
    private var binding: ItemLayoutBinding? = null
    private var habits: MutableList<Habit> = mutableListOf()

    //прикрепляем к родительскому xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        // описывается получение элемента списка
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding!!.root)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    //привязывает данные
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    //containerView - макет элеманта списка
    //Является подклассом RecyclerView.ViewHolder
    //??? как отличить подкласс от реализации интерфейса?
    //Имеет доступ к внешнему контексту
    //Этот внутренний класс используется для доступа к корневому представлению элемента списка
    inner class HabitViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                openHabitClick(habits[adapterPosition])
            }
        }
        fun bind(habit: Habit) {
            itemView.binding
        }
    }
}