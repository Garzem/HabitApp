package com.example.newapppp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newapppp.R
import com.example.newapppp.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {
    //это для ооп
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        val fab = binding.fab
        fab.setOnClickListener {
            navigateToRedactorFragment()
        }
    }

    private fun navigateToRedactorFragment() {
        val navController = findNavController()
        navController.navigate(R.id.nav_redactor_fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}