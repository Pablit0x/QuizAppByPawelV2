package com.example.quizappbypawel.fragments.list.teacher.quiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.TeacherListRowBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.model.Quiz
import kotlinx.android.synthetic.main.warning_dialog.*

/**
 * Adapter class for recyclerView inside fragment_teacher_list.xml
 */
class TeacherQuizListAdapter(private val parentFragment : TeacherQuizListFragment, private val activity : Activity) : RecyclerView.Adapter<TeacherQuizListAdapter.MyViewHolder>() {
    private var quizList = mutableListOf<Quiz>()

    class MyViewHolder(val binding: TeacherListRowBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TeacherListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = quizList[position]
        holder.binding.tvQuizID.text = currentItem.quizID
        holder.binding.tvQuizName.text = currentItem.quizName
        if(!validateQuiz(currentItem)){
            holder.binding.imgBtnQuizBtn.setImageResource(R.drawable.ic_baseline_warning_24)
            holder.binding.imgBtnQuizBtn.contentDescription = "Show Warning"
            holder.binding.imgBtnQuizBtn.setOnClickListener {
                showWarning(it.context)
            }
        }

        holder.binding.listRow.setOnClickListener {
            val action =
                TeacherQuizListFragmentDirections.actionTeacherListFragmentToTeacherQuestionListFragment(
                    currentItem
                )
            holder.binding.listRow.findNavController().navigate(action)
        }
    }
    /**
     * Function used to get the list of quizzes from TeacherQuizListFragment class
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(quiz: MutableList<Quiz>) {
        this.quizList = quiz
        notifyDataSetChanged()
    }
    /**
     * Checks if the given quiz fulfills the requirements to be classified as valid
     */
    private fun validateQuiz(quiz : Quiz) : Boolean{
        val questions = parentFragment.getQuizViewModel().getQuestionsForQuiz(quiz.quizID)
        var answers : List<Answer>
        val numberOfQuestion = questions.size
        var numberOfCorrectAnswers = 0
        if(questions.isEmpty()){
            return false
        } else{
            for(question in questions){
                answers = parentFragment.getQuizViewModel().getAnswersForQuestion(question.questionID)
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

    /**
     * Displays dialog with requirements that quiz has to meet to be visible to the student
     */
    @SuppressLint("SetTextI18n")
    private fun showWarning(context : Context){
        val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.warning_dialog, null)
        val dialog = AlertDialog.Builder(context)
            .setView(layoutInflater)
            .show()

        val dialogTitle = dialog.tvTitle
        val ruleOne = dialog.tvRuleOne
        val ruleTwo = dialog.tvRuleTwo
        val ruleThree = dialog.tvRuleThree

        dialogTitle.text = "Quiz is visible to the student if:"
        ruleOne.text = "1) Quiz contains at least one question"
        ruleTwo.text = "2) Each question contains at least two answers"
        ruleThree.text = "3) There is one correct answer for each question"

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}