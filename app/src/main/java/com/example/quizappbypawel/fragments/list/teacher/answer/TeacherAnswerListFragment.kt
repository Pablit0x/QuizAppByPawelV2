package com.example.quizappbypawel.fragments.list.teacher.answer

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
import com.example.quizappbypawel.databinding.FragmentTeacherAnswerListBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.swipeToDelete.SwipeToDelete
import com.example.quizappbypawel.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.create_answer_dialog.*
import kotlinx.android.synthetic.main.create_question_dialog.btnCancel
import kotlinx.android.synthetic.main.create_question_dialog.btnCreate

/**
 * TeacherAnswerListFragment class is responsible for displaying the list of answers which belong to the selected question
 */
class TeacherAnswerListFragment() : Fragment() {

    private lateinit var teacherAnswerListBinding: FragmentTeacherAnswerListBinding
    private lateinit var mQuizViewModel: QuizViewModel
    private val args by navArgs<TeacherAnswerListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teacher_answer_list, container, false)
        teacherAnswerListBinding = FragmentTeacherAnswerListBinding.bind(view)

        teacherAnswerListBinding.tvQuestion.text = args.selectedQuestion.question

        val adapter = TeacherAnswerListAdapter(this, requireActivity())
        val recyclerView = teacherAnswerListBinding.rvAnswers
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // UserViewModel
        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        mQuizViewModel.getALlAnswersFromQuestion(args.selectedQuestion.questionID)
            .observe(viewLifecycleOwner, { questionWithAnswers ->
                for (item in questionWithAnswers) {
                    adapter.setData(item.answers)
                }
            })

        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                deleteAnswer(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(teacherAnswerListBinding.rvAnswers)

        teacherAnswerListBinding.faBtnAddAnswer.setOnClickListener {
            addAnswer()
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
            R.id.menu_delete -> deleteAllAnswers()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Deletes all answers from current question
     */
    private fun deleteAllAnswers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mQuizViewModel.deleteAllAnswers()
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete all answers?")
        builder.setMessage("Are you sure you want to delete all answers?")
        builder.create().show()
    }

    /**
     * Deletes swiped answer
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteAnswer(pos: Int) {

        val selectedAnswer =
            mQuizViewModel.getAnswersForQuestion(args.selectedQuestion.questionID)[pos]
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mQuizViewModel.deleteAnswer(selectedAnswer)
            teacherAnswerListBinding.rvAnswers.adapter?.notifyItemRemoved(pos)
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            teacherAnswerListBinding.rvAnswers.adapter?.notifyDataSetChanged()
        }
        builder.setTitle("Delete ${selectedAnswer.answer}?")
        builder.setMessage("Are you sure you want to delete ${selectedAnswer.answer}?")
        builder.create().show()
    }

    /**
     * Redirects teacher to the CreateAnswerFragment Fragment
     * Makes sure that the teacher is unable to add more than 10 answers to each question
     */
    private fun addAnswer() {
        val numberOfAnswers = mQuizViewModel.getAnswersForQuestion(args.selectedQuestion.questionID).size
        if(numberOfAnswers < 10){
            showDialog()
        } else{
            Toast.makeText(requireContext(), "You cannot add more than 10 answers!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Changes the fragment navigation to the ModeSelectionFragment Fragment
     */
    private fun navigateHome() {
        val action =
            TeacherAnswerListFragmentDirections.actionTeacherAnswerListFragmentToModeSelectionFragment()
        findNavController().navigate(action)
    }

    /**
     * Checks if the answer text field is not empty
     */
    private fun inputCheck(answer: String): Boolean {
        return !(TextUtils.isEmpty(answer))
    }

    /**
     * Inserts answer data to the answer_table
     */
    private fun insertData(answerText : String, isAnswerCorrect : Boolean) : Boolean {

        return if (inputCheck(answerText)) {
            if(isAnswerCorrect){
                setIsCorrectToDefault(args.selectedQuestion.questionID)
            }
            val answer = Answer(
                answer = answerText,
                isCorrect = isAnswerCorrect,
                questionID = args.selectedQuestion.questionID
            )
            mQuizViewModel.addAnswer(answer)
            true
        } else {
            Toast.makeText(requireContext(), "Fill out answer field!", Toast.LENGTH_LONG).show()
            false
        }
    }

    /**
     * Sets isCorrect field in every answer object included in given questions to false
     */
    fun setIsCorrectToDefault(questionID : Int){
        val answers = mQuizViewModel.getAnswersForQuestion(questionID)
        for(answer in answers){
            val updatedAnswer = Answer(answerID = answer.answerID, answer = answer.answer, isCorrect = false, questionID = answer.questionID)
            mQuizViewModel.updateAnswer(updatedAnswer)
        }
    }

    /**
     * Displays the dialog that lets the user create a answer
     */
    private fun showDialog() {
        val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.create_answer_dialog, null)
        val dialog = AlertDialog.Builder(context)
            .setView(layoutInflater)
            .show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnCreate.setOnClickListener {
            val answer = dialog.etAnswerInput.text.toString()
            val isCorrect = dialog.sIsCorrect.isChecked
            if(insertData(answer, isCorrect)){
                dialog.dismiss()
            }
        }
    }

    fun getViewModel() : QuizViewModel{
        return mQuizViewModel
    }

}