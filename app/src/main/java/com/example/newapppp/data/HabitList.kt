package com.example.newapppp.data

object HabitList {

    private val habitsList = mutableListOf<Habit>()

    fun addHabit(habit: Habit) = habitsList.add(habit)

    fun updateHabit(habit: Habit, index: Int) = habitsList.set(index, habit)
    //??
    fun deleteHabit(index: Int) {
        habitsList.apply { drop(index) }
    }

    fun getHabits() = habitsList.toList()

    fun getHabit(index: Int) = habitsList[index]
}