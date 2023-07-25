package com.example.newapppp.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.data.HabitColor
import com.example.newapppp.databinding.ActivityMainBinding
import com.example.newapppp.databinding.HorizontalColorChooseBinding
import com.example.newapppp.ui.redactor.RedactorFragmentViewModel

class ColorChooseDialog : DialogFragment(R.layout.horizontal_color_choose) {

    private var buttons: ArrayList<View>? = null
    private val binding by viewBinding(HorizontalColorChooseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //получает все элементы доступные для нажатия
        buttons = binding.linearColorButtons.touchables
        buttons?.forEachIndexed { i, button ->
            button.setOnClickListener {
                findNavController().apply {
                    popBackStack()
                    val entry = currentBackStackEntry ?: return@apply
                    entry.savedStateHandle.set("color", HabitColor.values()[i])
                }
            }
        }
    }
}
