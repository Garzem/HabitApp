package com.example.newapppp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newapppp.R
import com.example.newapppp.databinding.HomeFragmentBinding
import androidx.navigation.fragment.findNavController
import com.example.newapppp.data.Type


class HomeFragment : Fragment() {

    companion object {
        //создаётся в качестве ключа сохранения в Bundle() для habit_type
        const val HABIT_TYPE = "habit_type"
        //создаёт новый экземпляр текущего фрагмента
        fun newInstance(habitType: Type): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            //помещаем объекты, которые можно сериализовать
            bundle.putSerializable(HABIT_TYPE,habitType)
            //передаём данные в специально предназначенное для них место
            fragment.arguments = bundle
            return fragment
        }
    }
    //это для ооп
    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)
        val fab = binding.fab
        fab.setOnClickListener {
            navigateToRedactorFragment()
        }
        //получение данных о привычке
    }

    private fun navigateToRedactorFragment() {
        findNavController().navigate(R.id.action_home_fragment_to_redactor_fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}