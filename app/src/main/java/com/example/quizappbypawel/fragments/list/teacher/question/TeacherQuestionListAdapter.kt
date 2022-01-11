package com.example.quizappbypawel.fragments.list.teacher.question

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.databinding.QuestionListRowBinding
import com.example.quizappbypawel.model.Question

/**
 * Adapter class for recyclerView inside fragment_teacher_question_list.xml
 */
class TeacherQuestionListAdapter(private val args: TeacherQuestionListFragmentArgs) :
    RecyclerView.Adapter<TeacherQuestionListAdapter.MyViewHolder>() {

    private var questionList = emptyList<Question>()

    class MyViewHolder(val binding: QuestionListRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            QuestionListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = questionList[position]
        holder.binding.tvQuestion.text = currentItem.question
        holder.binding.questionListRow.setOnClickListener {
            val action =
                TeacherQuestionListFragmentDirections.actionTeacherQuestionListFragmentToTeacherAnswerListFragment(
                    currentItem,
                    args.selectedQuiz
                )
            holder.binding.questionListRow.findNavController().navigate(action)
        }
    }
    /**
     * Function used to get the list of questions from TeacherQuestionListFragment class
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(question: List<Question>) {
        this.questionList = question
        notifyDataSetChanged()
    }
}