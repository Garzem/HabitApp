package com.example.newapppp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.common.ui.element.button.BaseButton
import com.example.newapppp.common.ui.theme.HabitTheme
import com.example.newapppp.common.ui.theme.color.HabitAppColors
import com.example.newapppp.databinding.HorizontalColorChooseDialogBinding
import com.example.newapppp.domain.model.HabitColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorChooseDialog : DialogFragment(R.layout.horizontal_color_choose_dialog) {

    private var buttons: ArrayList<View>? = null
    private val binding by viewBinding(HorizontalColorChooseDialogBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = binding.linearColorButtons.touchables
        buttons?.forEachIndexed { i, button ->
            button.setOnClickListener {
                findNavController().apply {
                    popBackStack()
                    val entry = currentBackStackEntry ?: return@apply
                    entry.savedStateHandle[com.example.newapppp.domain.Constants.COLOR_KEY] =
                        HabitColor.values()[i]
                }
            }
        }
    }

    @Composable
    fun ColorChooseScreen() {

    }

    @SuppressLint("NotConstructor")
    @Composable
    fun ColorChooseDialog() {
        val colors = listOf(
            HabitAppColors.Pink,
            HabitAppColors.Red,
            HabitAppColors.OrangeRed,
            HabitAppColors.Orange,
            HabitAppColors.Saffron,
            HabitAppColors.Amber,
            HabitAppColors.LawnGreen,
            HabitAppColors.Mantis,
            HabitAppColors.Emerald,
            HabitAppColors.Aqua,
            HabitAppColors.BlueLight,
            HabitAppColors.Blue,
            HabitAppColors.BlueDark1,
            HabitAppColors.BlueDark2,
            HabitAppColors.Purple,
            HabitAppColors.PurpleDeep,
            HabitAppColors.PurpleDark
        )
        
//        Dialog(onDismissRequest = { onDismiss() }) {
//
//        }
    }

    @Composable
    fun ColorButton(
        color: Color,
        onClicked: () -> Unit
    ) {
        BaseButton(
            modifier = Modifier
                .size(HabitTheme.dimensions.colorButtonWidth)
                .background(color),
            onClick = onClicked,
        )
    }
}


