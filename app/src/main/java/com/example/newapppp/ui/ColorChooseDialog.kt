package com.example.newapppp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.Constants
import com.example.newapppp.data.HabitColor
import com.example.newapppp.databinding.HorizontalColorChooseBinding


class ColorChooseDialog : DialogFragment(R.layout.horizontal_color_choose) {

    private var buttons: ArrayList<View>? = null
    private val binding by viewBinding(HorizontalColorChooseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = binding.linearColorButtons.touchables
        buttons?.forEachIndexed { i, button ->
            button.setOnClickListener {
                findNavController().apply {
                    popBackStack()
                    val entry = currentBackStackEntry ?: return@apply
                    entry.savedStateHandle[Constants.COLOR_KEY] = HabitColor.values()[i]
                }
            }
        }
    }
}


