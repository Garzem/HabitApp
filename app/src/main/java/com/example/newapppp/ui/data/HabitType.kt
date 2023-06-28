package com.example.newapppp.ui.data

enum class HabitType(val value: Int) {
    GOOD(0),
    BAD(1);

    //this это HabitType
    override fun toString(): String {
        return if (this == BAD) {
            "Вредная"
        } else {
            "Полезная"
        }
    }

    companion object
}