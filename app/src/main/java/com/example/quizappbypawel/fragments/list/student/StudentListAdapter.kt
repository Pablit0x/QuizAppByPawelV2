package com.example.quizappbypawel.fragments.list.student

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.model.Quiz
import androidx.navigation.findNavController
import com.example.quizappbypawel.databinding.StudentListRowBinding

/**
 * Adapter class for recyclerView inside fragment_student_list.xml
 */
class StudentListAdapter : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {

    private var quizList = mutableListOf<Quiz>()

    class MyViewHolder(val binding: StudentListRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            StudentListRowBinding.inflate(
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

        holder.binding.listRow.setOnClickListener {
            showAlertDialog(holder,currentItem,it.context)
        }
    }

    /**
     * Displays the popup dialog after the student clicks on the quiz item
     */
    private fun showAlertDialog(holder: MyViewHolder, quiz: Quiz, context: Context) { // receive the context here
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes") { _, _ ->
            val action =
                StudentListFragmentDirections.actionStudentListFragmentToStudentQuizFragment(quiz)
            holder.binding.listRow.findNavController().navigate(action)
        }
        builder.setNegativeButton("No") { _, _ ->
            // do nothing
        }
        builder.setMessage("Do you want to start ${quiz.quizName} quiz?")
        builder.create().show()
    }

    /**
     * Function used to get mutable list of quizzes from StudentListFragment class
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(quiz: MutableList<Quiz>) {
        this.quizList = quiz
        notifyDataSetChanged()
    }
}