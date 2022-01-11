package com.example.quizappbypawel.fragments.list.student.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentStudentQuizBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.model.Question
import com.example.quizappbypawel.viewmodel.QuizViewModel

class StudentQuizFragment : Fragment() {
    private lateinit var studentQuizBiding : FragmentStudentQuizBinding
    private lateinit var mQuizViewModel: QuizViewModel
    private val args by navArgs<StudentQuizFragmentArgs>()
    private lateinit var questions : List<Question>
    lateinit var adapter : StudentQuizAdapter
    private var selectedAnswers : MutableList<Int> = mutableListOf()
    private var sAnswer = -1
    private var score = 0
    private var numberOfQuestions = 0
    private var answers : MutableList<Answer> = mutableListOf()
    private var questionCounter = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_quiz, container, false)
        studentQuizBiding = FragmentStudentQuizBinding.bind(view)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.selectedQuiz.quizName

        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        questions = mQuizViewModel.getQuestionsForQuizRandomized(args.selectedQuiz.quizID)
        numberOfQuestions = questions.size
        answers.addAll(mQuizViewModel.getAnswersForQuestion(questions[questionCounter].questionID))

        studentQuizBiding.tvQuestion.text = questions[questionCounter].question

        adapter = StudentQuizAdapter(this)
        val recyclerView = studentQuizBiding.rvAnswers
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        adapter.setData(answers.toList())

        studentQuizBiding.btnSubmit.setOnClickListener {
            if(questionCounter + 1 < questions.size){
                addSelectedAnswerToMutableList(selectedAnswer = sAnswer)
                updateView()
            } else{
                addSelectedAnswerToMutableList(selectedAnswer = sAnswer)
                val action = StudentQuizFragmentDirections.actionStudentQuizFragmentToQuizEndFragment(score, questions.size)
                findNavController().navigate(action)
            }
        }

        return view
    }

    private fun updateView(){
        questionCounter++
        sAnswer = -1
        adapter.resetPosition()
        answers.clear()
        answers.addAll(mQuizViewModel.getAnswersForQuestion(questions[questionCounter].questionID))
        studentQuizBiding.tvQuestion.text = questions[questionCounter].question
        adapter.setData(answers.toList())
    }

    private fun addSelectedAnswerToMutableList(selectedAnswer : Int){
        selectedAnswers.add(selectedAnswer)
        if(selectedAnswer != -1){
            val a = mQuizViewModel.getAnswersForQuestion(questions[questionCounter].questionID)[selectedAnswer].isCorrect
            if(a){
                score++
            }
        } }

    fun setSelectedAnswer(selectedAnswer : Int){
        this.sAnswer = selectedAnswer
    }
}