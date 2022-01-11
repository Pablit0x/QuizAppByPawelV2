package com.example.quizappbypawel.fragments.end

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentQuizEndBinding

/**
 * QuizEndFragment class manages the congratulation screen which is displayed at the end of each quiz
 */
class QuizEndFragment : Fragment() {
    private val args by navArgs<QuizEndFragmentArgs>()

    private lateinit var quizEndBinding: FragmentQuizEndBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_end, container, false)

        quizEndBinding = FragmentQuizEndBinding.bind(view)

        quizEndBinding.btnFinish.setOnClickListener {
            val action = QuizEndFragmentDirections.actionQuizEndFragmentToStudentListFragment()
            findNavController().navigate(action)
        }

        quizEndBinding.tvScore.text = "Score: ${args.score}/${args.numOfQuestions}"

        return view
    }
}