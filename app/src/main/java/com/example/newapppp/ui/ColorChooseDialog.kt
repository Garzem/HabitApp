package com.example.newapppp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.AppHabitDataBase
import com.example.newapppp.data.Constants
import com.example.newapppp.data.HabitColor
import com.example.newapppp.data.HabitDao
import com.example.newapppp.databinding.HorizontalColorChooseBinding
import kotlinx.coroutines.launch


class ColorChooseDialog : DialogFragment(R.layout.horizontal_color_choose) {

    private var buttons: ArrayList<View>? = null
    private val binding by viewBinding(HorizontalColorChooseBinding::bind)
    private val habitDao: HabitDao = AppHabitDataBase.getDatabase().habitDao()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = binding.linearColorButtons.touchables
        buttons?.forEachIndexed { i, button ->
            button.setOnClickListener {
                lifecycleScope.launch {
                    habitDao.upsertColor(HabitColor.values()[i])
                }
            }
        }
    }
}

