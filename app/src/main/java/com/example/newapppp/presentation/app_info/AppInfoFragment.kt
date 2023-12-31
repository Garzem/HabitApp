package com.example.newapppp.presentation.app_info

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.newapppp.R
import com.example.newapppp.databinding.AppInfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppInfoFragment : Fragment(R.layout.app_info_fragment) {
    private val binding by viewBinding(AppInfoFragmentBinding::bind)
}