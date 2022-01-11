package com.example.quizappbypawel.fragments.mode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentModeSelectionBinding

class ModeSelectionFragment : Fragment() {
    private lateinit var modeSelectionBinding: FragmentModeSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mode_selection, container, false)
        modeSelectionBinding = FragmentModeSelectionBinding.bind(view)

        modeSelectionBinding.btnTeacher.setOnClickListener {
            findNavController().navigate(R.id.action_modeSelectionFragment_to_teacherListFragment)
        }

        modeSelectionBinding.btnStudent.setOnClickListener {
            findNavController().navigate(R.id.action_modeSelectionFragment_to_studentListFragment)
        }

        return view
    }
}