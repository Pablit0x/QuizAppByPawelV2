package com.example.quizappbypawel.fragments.list.teacher.question

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentTeacherQuestionListBinding
import com.example.quizappbypawel.model.Question
import com.example.quizappbypawel.swipeToDelete.SwipeToDelete
import com.example.quizappbypawel.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.create_question_dialog.*

/**
 * TeacherQuestionListFragment class is responsible for displaying the list of questions that belong to the selected quiz
 */
class TeacherQuestionListFragment : Fragment() {
    private lateinit var teacherQuestionBiding: FragmentTeacherQuestionListBinding
    private lateinit var mQuizViewModel: QuizViewModel
    private val args by navArgs<TeacherQuestionListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_question_list, container, false)
        teacherQuestionBiding = FragmentTeacherQuestionListBinding.bind(view)

        teacherQuestionBiding.tvQuizNameQuestionList.text = args.selectedQuiz.quizName

        val adapter = TeacherQuestionListAdapter(args)
        val recyclerView = teacherQuestionBiding.rvQuestions
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        mQuizViewModel.getALlQuestionsFromQuiz(args.selectedQuiz.quizID)
            .observe(viewLifecycleOwner, { quizWithQuestions ->
                for (item in quizWithQuestions) {
                    adapter.setData(item.questions)
                }
            })

        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                deleteQuestion(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(teacherQuestionBiding.rvQuestions)

        teacherQuestionBiding.faBtnAddQuestion.setOnClickListener {
            showDialog()
        }

        setHasOptionsMenu(true)

        return view
    }

    /**
     * Inflates the menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.teacher_menu, menu)
    }

    /**
     * Handles menu icon clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> navigateHome()
            R.id.menu_delete -> deleteAllQuestions()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Deletes all questions from current quiz
     */
    private fun deleteAllQuestions() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mQuizViewModel.deleteAllQuestions()
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete all questions?")
        builder.setMessage("Are you sure you want to delete all questions?")
        builder.create().show()
    }
    /**
     * Deletes swiped question
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteQuestion(pos: Int) {
        val selectedQuestion = mQuizViewModel.getQuestionsForQuiz(args.selectedQuiz.quizID)[pos]
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mQuizViewModel.deleteQuestion(selectedQuestion)
            teacherQuestionBiding.rvQuestions.adapter?.notifyItemRemoved(pos)
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            teacherQuestionBiding.rvQuestions.adapter?.notifyDataSetChanged()
        }
        builder.setTitle("Delete ${selectedQuestion.question}?")
        builder.setMessage("Are you sure you want to delete ${selectedQuestion.question}?")
        builder.create().show()
    }

    /**
     * Changes the fragment navigation to the ModeSelectionFragment Fragment
     */
    private fun navigateHome() {
        val action =
            TeacherQuestionListFragmentDirections.actionTeacherQuestionListFragmentToModeSelectionFragment()
        findNavController().navigate(action)
    }

    /**
     * Displays the dialog that lets the user create a question
     */
    private fun showDialog() {
        val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.create_question_dialog, null)
        val dialog = AlertDialog.Builder(context)
            .setView(layoutInflater)
            .show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnCreate.setOnClickListener {
            val question = dialog.etQuestion.text.toString()
            if(insertData(question)){
                dialog.dismiss()
            }
        }
    }
    /**
     * Checks if the question text field is not empty
     */
    private fun inputCheck(question: String): Boolean {
        return !(TextUtils.isEmpty(question))
    }

    /**
     * Inserts question data to the question_table
     */
    private fun insertData(questionText : String) : Boolean {

        return if (inputCheck(questionText)) {
            val question = Question(question = questionText, quizID = args.selectedQuiz.quizID)
            mQuizViewModel.addQuestion(question)
            true
        } else {
            Toast.makeText(requireContext(), "Fill out question field!", Toast.LENGTH_LONG).show()
            false
        }
    }
}