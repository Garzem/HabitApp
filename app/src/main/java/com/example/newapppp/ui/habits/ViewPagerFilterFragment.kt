package com.example.newapppp.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newapppp.R

class ViewPagerFilterFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.redactor_fragment, container, false)

        //??как я могу вызвать arraylist, если в adapter у меня в конструкторе стоит list?
    }
}