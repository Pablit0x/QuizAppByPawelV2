package com.example.quizappbypawel.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.quizappbypawel.data.QuizDatabase
import com.example.quizappbypawel.model.*
import com.example.quizappbypawel.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<MutableList<Quiz>>
    private val readAllQuestionData: LiveData<List<Question>>
    private val readAllAnswerData: LiveData<List<Answer>>
    private val readAllAnswerDataRandomized: LiveData<List<Answer>>
    private val quizRepository: QuizRepository

    init {
        val quizDao = QuizDatabase.getDatabase(
            application
        ).quizDao()
        quizRepository = QuizRepository(quizDao)
        readAllAnswerData = quizRepository.readAllAnswerData
        readAllAnswerDataRandomized = quizRepository.readAllAnswerDataRandomized
        readAllQuestionData = quizRepository.readAllQuestionData
        readAllData = quizRepository.readAllData
    }

    fun addQuiz(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.addQuiz(quiz)
        }
    }

    fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.updateQuiz(quiz)
        }
    }

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteQuiz(quiz)
        }
    }

    fun deleteAllQuizzes() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteAllQuizzes()
        }
    }

    fun addQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.addQuestion(question)
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.updateQuestion(question)
        }
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteQuestion(question)
        }
    }

    fun deleteAllQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteAllQuestions()
        }
    }

    fun addAnswer(answer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.addAnswer(answer)
        }
    }

    fun updateAnswer(answer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.updateAnswer(answer)
        }
    }

    fun deleteAnswer(answer: Answer) {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteAnswer(answer)
        }
    }

    fun deleteAllAnswers() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.deleteAllAnswers()
        }
    }

    fun getALlAnswersFromQuestion(questionID: Int): LiveData<List<QuestionWithAnswers>> {
        return quizRepository.getALlAnswersFromQuestion(questionID)
    }

    fun getALlQuestionsFromQuiz(quizID: String): LiveData<List<QuizWithQuestions>> {
        return quizRepository.getALlQuestionsFromQuiz(quizID)
    }

    fun getQuestionsForQuiz(quizID: String): List<Question> {
        return quizRepository.getQuestionsForQuiz(quizID)
    }

    fun getAnswersForQuestion(questionID: Int): List<Answer> {
        return quizRepository.getAnswersForQuestion(questionID)
    }

    fun getQuestionsForQuizRandomized(quizID: String): List<Question>{
        return quizRepository.getQuestionsForQuizRandomized(quizID)
    }

}