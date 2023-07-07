package com.example.newapppp.data

import android.os.Parcel
import android.os.Parcelable

data class Habit(
    val title: String?,
    val description: String,
    val period: String,
    val color: Int,
    val priority: Priority,
    val type: Type,
    val quantity: String
) : Parcelable {
    //??Чей это конструктор (Habit или Parcelable)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        //считывает целочисленное значение из Parcel, а затем
        //используем его в качестве индекса для доступа к
        //соответствующему элементу перечисления Priority
        Priority.values()[parcel.readInt()],
        Type.values()[parcel.readInt()],
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(period)
        dest.writeInt(color)
        //ordinal возвращает порядковый номер перечесления начиная с нуля
        dest.writeInt(priority.ordinal)
        dest.writeInt(type.ordinal)
        dest.writeString(quantity)
    }

    //??(подробней)используется для создания экземпляра класса Habit из Parcel
    //и для создания массива экземпляров Habit
    companion object CREATOR : Parcelable.Creator<Habit> {
        //используется для создания экземпляра класса Habit из Parcel
        //вызывается при восстановлении объекта Habit из Parcel
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }
        //используется для создания массива экземпляров Habi
        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Priority {
    CHOOSE,
    LOW,
    MEDIUM,
    HIGH
}

enum class Type {
    GOOD,
    BAD
}
