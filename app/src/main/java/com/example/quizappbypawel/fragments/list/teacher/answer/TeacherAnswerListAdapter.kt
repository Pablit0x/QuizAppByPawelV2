package com.example.quizappbypawel.fragments.list.teacher.answer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.databinding.AnswerListRowBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.R


/**
 * Adapter class for recyclerView inside fragment_teacher_answer_list.xml
 */
class TeacherAnswerListAdapter(private val parentFragment : TeacherAnswerListFragment, private val activity : Activity): RecyclerView.Adapter<TeacherAnswerListAdapter.MyViewHolder>() {

    private var answerList = emptyList<Answer>()

    class MyViewHolder(val binding: AnswerListRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AnswerListRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var correctAnswerIndex = -1
        val currentItem = answerList[position]

        holder.binding.answerListRow.setOnClickListener {
            setCorrectAnswer(currentItem)
        }
        if(currentItem.isCorrect){
            correctAnswerIndex = position
        }
        var i = 0
        holder.binding.tvAnswer.text = currentItem.answer
        for(answer in answerList){
            i++
            if(i != correctAnswerIndex)
                holder.binding.answerListRow.setBackgroundResource(R.color.light_grey)
                holder.binding.tvAnswer.setTextColor(Color.BLACK)
        }
        if(currentItem.isCorrect){
            holder.binding.answerListRow.setBackgroundResource(R.color.dark_green)
            holder.binding.tvAnswer.setTextColor(Color.WHITE)
        }
    }
    /**
     * Function used to get the list of answers from TeacherAnswerListFragment class
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(answer: List<Answer>) {
        this.answerList = answer
        notifyDataSetChanged()
    }

    private fun setCorrectAnswer(selectedAnswer : Answer) {
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("Yes") { _, _ ->
            val updatedAnswer = Answer(selectedAnswer.answerID, selectedAnswer.answer, true, selectedAnswer.questionID)
            parentFragment.setIsCorrectToDefault(selectedAnswer.questionID)
            parentFragment.getViewModel().updateAnswer(updatedAnswer)
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.setTitle("Set ${selectedAnswer.answer} as the correct answer?")
        builder.create().show()
    }
}