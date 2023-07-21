package com.example.newapppp.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.newapppp.R
import com.example.newapppp.databinding.HorizontalColorChooseBinding

class ColorChooseDialog : DialogFragment() {

    private var buttons: ArrayList<View>? = null
    private var binding: HorizontalColorChooseBinding? = null
    private var onInputListener: OnInputListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.horizontal_color_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //bind принимает представление view в качестве параметра
        //и создает экземпляр привязки (binding) для этого представления
        binding = HorizontalColorChooseBinding.bind(view)
        //получает все элементы доступные для нажатия
        buttons = binding?.linearColorButtons?.touchables
        buttons?.forEach { button ->
            button.setOnClickListener {
                //it указывается для того, чтобы не учитывать нажатий по другим элементам, кроме button
                val color = getColorFromButton(it as Button)
                if (color != null) {
                    onInputListener?.sendColor(color)
                }
                dismiss()
            }
        }
    }

    interface OnInputListener {
        fun sendColor(colorChoose: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //??Что является supportFragmentManager и как программа это понимает
        val fragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main)!!
            .childFragmentManager.fragments[0]
        onInputListener = fragment as OnInputListener
    }

    private fun getColorFromButton(button: Button): Int? {
        val background = button.background
        return if (background is GradientDrawable) {
            background.color?.defaultColor
        } else {
            null
        }
    }
}
