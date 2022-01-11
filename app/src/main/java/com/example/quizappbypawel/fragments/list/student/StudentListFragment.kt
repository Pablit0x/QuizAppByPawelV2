package com.example.quizappbypawel.fragments.list.student

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentStudentListBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.model.Quiz
import com.example.quizappbypawel.viewmodel.QuizViewModel
import java.util.*

/**
 * StudentListFragment class is responsible for displaying the list of validated quizzes to the users that identified as a students
 *
 */
class StudentListFragment : Fragment() {
    private lateinit var studentListBinding: FragmentStudentListBinding
    private lateinit var mQuizViewModel: QuizViewModel
    private var quizListWithFilter = mutableListOf<Quiz>()
    private var initialQuizList = mutableListOf<Quiz>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        studentListBinding = FragmentStudentListBinding.bind(view)

        val adapter = StudentListAdapter()
        val recyclerView = studentListBinding.rvQuestionBank
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        mQuizViewModel.readAllData.observe(viewLifecycleOwner, { quizzes ->
            val quizzesList : MutableList<Quiz> = arrayListOf()
            for(quiz in quizzes){
                if(validateQuiz(quiz)){
                    quizzesList.add(quiz)
                }
            }
            quizListWithFilter.clear()
            initialQuizList.addAll(quizzesList)
            quizListWithFilter.addAll(quizzesList)

            adapter.setData(quizListWithFilter)
        })

        setHasOptionsMenu(true)

        return view
    }

    /**
     * Inflates the menu and handles the search logic for the recyclerView items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.menu_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                quizListWithFilter.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotBlank() && searchText.isNotEmpty()){
                    initialQuizList.forEach{
                        if(it.quizName.lowercase(Locale.getDefault()).contains(searchText))
                            quizListWithFilter.add(it)
                    }
                    studentListBinding.rvQuestionBank.adapter?.notifyDataSetChanged()

                } else{
                    quizListWithFilter.clear()
                    quizListWithFilter.addAll(initialQuizList)
                    studentListBinding.rvQuestionBank.adapter?.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    /**
     * Checks if the given quiz fulfills the requirements to be classified as valid
     */
    private fun validateQuiz(quiz : Quiz) : Boolean{
        val questions = mQuizViewModel.getQuestionsForQuiz(quiz.quizID)
        var answers : List<Answer>
        val numberOfQuestion = questions.size
        var numberOfCorrectAnswers = 0
        if(questions.isEmpty()){
            return false
        } else{
            for(question in questions){
                answers = mQuizViewModel.getAnswersForQuestion(question.questionID)
                if(answers.size < 2){
                    return false
                }
                for(answer in answers){
                    if(answer.isCorrect){
                        numberOfCorrectAnswers++
                    }
                }
            }
            if(numberOfCorrectAnswers < numberOfQuestion){
                return false
            }
        }
        return true
    }


}