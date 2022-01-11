package com.example.quizappbypawel.fragments.list.teacher.quiz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizappbypawel.R
import com.example.quizappbypawel.databinding.FragmentTeacherListBinding
import com.example.quizappbypawel.model.Quiz
import com.example.quizappbypawel.swipeToDelete.SwipeToDelete
import com.example.quizappbypawel.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.create_quiz_dialog.*
import java.util.*


/**
 * TeacherQuizListFragment class is responsible for displaying the list of quizzes
 * The quizzes that are classified as valid will show green check icon next to their names
 * The quizzes that are not classified as valid will show red warning icon next to their names
 */
class TeacherQuizListFragment : Fragment() {
    private lateinit var teacherListBinding: FragmentTeacherListBinding
    private lateinit var mQuizViewModel: QuizViewModel
    private var quizListWithFilter = mutableListOf<Quiz>()
    private var initialQuizList = mutableListOf<Quiz>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_list, container, false)
        teacherListBinding = FragmentTeacherListBinding.bind(view)

        val adapter = TeacherQuizListAdapter(this,requireActivity())
        val recyclerView = teacherListBinding.rvQuestionBankTeacher
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // UserViewModel
        mQuizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        mQuizViewModel.readAllData.observe(viewLifecycleOwner, { quizzes ->
            quizListWithFilter.clear()
            initialQuizList.clear()
            initialQuizList.addAll(quizzes)
            quizListWithFilter.addAll(quizzes)
            adapter.setData(quizListWithFilter)
        })

        teacherListBinding.faBtnAddNewQuestionBank.setOnClickListener {
//            findNavController().navigate(R.id.action_teacherListFragment_to_createQuizFragment)
            showDialog()
        }

        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                deleteQuiz(pos)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(teacherListBinding.rvQuestionBankTeacher)


        setHasOptionsMenu(true)

        return view
    }

    /**
     * Inflates the menu and handles the search logic for the recyclerView items
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.teacher_menu_with_search, menu)
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
                    teacherListBinding.rvQuestionBankTeacher.adapter?.notifyDataSetChanged()
                } else{
                    quizListWithFilter.clear()
                    quizListWithFilter.addAll(initialQuizList)
                    teacherListBinding.rvQuestionBankTeacher.adapter?.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    /**
     * Handles menu icon clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> navigateHome()
            R.id.menu_delete -> deleteAllQuizzes()
        }
        return super.onOptionsItemSelected(item)
    }
    /**
     * Deletes all quizzes
     */
    private fun deleteAllQuizzes() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mQuizViewModel.deleteAllQuizzes()
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete all quizzes?")
        builder.setMessage("Are you sure you want to delete all quizzes?")
        builder.create().show()
    }
    /**
     * Deletes swiped quiz
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteQuiz(pos: Int) {
        val selectedQuiz = mQuizViewModel.readAllData.value?.get(pos)
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            selectedQuiz.let {
                if (it != null) {
                    mQuizViewModel.deleteQuiz(it)
                }
            }
            teacherListBinding.rvQuestionBankTeacher.adapter?.notifyItemRemoved(pos)
            Toast.makeText(
                requireContext(),
                "Successfully removed!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            teacherListBinding.rvQuestionBankTeacher.adapter?.notifyDataSetChanged()
        }
        builder.setTitle("Delete ${selectedQuiz!!.quizName}?")
        builder.setMessage("Are you sure you want to delete ${selectedQuiz.quizName}?")
        builder.create().show()
    }

    /**
     * Changes the fragment navigation to the ModeSelectionFragment Fragment
     */
    private fun navigateHome() {
        val action =
            TeacherQuizListFragmentDirections.actionTeacherListFragmentToModeSelectionFragment()
        findNavController().navigate(action)
    }

    /**
     * Passes the QuizViewModel object to the TeacherQuizListAdapter class
     */
    fun getQuizViewModel() : QuizViewModel{
        return mQuizViewModel
    }

    /**
     * Displays the dialog that lets the user create a quiz
     */
    private fun showDialog() {
        val layoutInflater = LayoutInflater.from(activity).inflate(R.layout.create_quiz_dialog, null)
        val dialog = AlertDialog.Builder(context)
            .setView(layoutInflater)
            .show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnCreate.setOnClickListener {
            var alreadyExist = false
            val quizID = dialog.etQuizID.text.toString()
            val quizName = dialog.etQuizName.text.toString()
            mQuizViewModel.readAllData.observe(viewLifecycleOwner,{quiz ->
                for(q in quiz){
                    if(q.quizID == quizID){
                        alreadyExist = true
                    }
                }
            })
            if(alreadyExist){
                Toast.makeText(requireContext(), "Quiz with ID: $quizID already exist!",Toast.LENGTH_SHORT).show()
            } else{
                if(insertData(quizID, quizName)){
                    dialog.dismiss()
                }
            }
        }
    }

    /**
     * Checks if the quizName and quizID text fields are not empty
     */
    private fun inputCheck(quizID: String, quizName: String): Boolean {
        return !TextUtils.isEmpty(quizID) && !TextUtils.isEmpty(quizName)
    }

    /**
     * Inserts quiz data to the quiz_table
     */
    private fun insertData(quizID: String, quizName : String) : Boolean{

        return if (inputCheck(quizID, quizName)) {
            val quiz = Quiz(
                quizID,
                quizName
            )
            mQuizViewModel.addQuiz(quiz)
            true
        } else {
            Toast.makeText(requireContext(), "Fill out all fields!", Toast.LENGTH_LONG).show()
            false
        }
    }
}