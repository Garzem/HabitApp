package com.example.newapppp.ui.home

import android.os.Build
import android.os.Bundle
import java.io.Serializable

class HomeSerializable {
    companion object {
        //расширяет класс Bundle новой функцией и говорит о том, она может взаимодествовать только с
        //подтипами(T) интерфейса Serializable, это нужно для того, чтобы код ниже был валиден в тех,
        //местах, где он будет применяться, т.е для подтипов Serializable
        fun <T : Serializable> Bundle.serialazible(key: String, className: Class<T>): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                getSerializable(key, className)
            else
                getSerializable(key) as? T
        }
    }
}