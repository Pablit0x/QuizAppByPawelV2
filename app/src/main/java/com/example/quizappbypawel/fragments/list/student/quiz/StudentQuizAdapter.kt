package com.example.quizappbypawel.fragments.list.student.quiz

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.databinding.AnswerListRowBinding
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.R


class StudentQuizAdapter(private val parentFragment : StudentQuizFragment) : RecyclerView.Adapter<StudentQuizAdapter.MyViewHolder>() {

    private var answerList = emptyList<Answer>()
    private var selectedPosition = -1

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentItem = answerList[position]
        holder.binding.tvAnswer.text = currentItem.answer

        holder.binding.answerListRow.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
        if (selectedPosition == position) {
            holder.binding.answerListRow.setBackgroundResource(R.color.dark_green)
            holder.binding.tvAnswer.setTextColor(Color.WHITE)
            parentFragment.setSelectedAnswer(selectedPosition)
        } else {
            holder.binding.answerListRow.setBackgroundResource(R.color.light_grey)
            holder.binding.tvAnswer.setTextColor(Color.BLACK)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(answer: List<Answer>) {
        this.answerList = answer
        notifyDataSetChanged()
    }

    fun resetPosition(){
        selectedPosition = -1
    }

}