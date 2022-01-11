package com.example.quizappbypawel.repository

import androidx.lifecycle.LiveData
import com.example.quizappbypawel.data.QuizDao
import com.example.quizappbypawel.model.*

class QuizRepository(private val quizDao: QuizDao) {
    val readAllData: LiveData<MutableList<Quiz>> = quizDao.readAllData()
    val readAllQuestionData: LiveData<List<Question>> = quizDao.readAllQuestionData()
    val readAllAnswerData: LiveData<List<Answer>> = quizDao.readAllAnswerData()
    val readAllAnswerDataRandomized: LiveData<List<Answer>> = quizDao.readAllAnswerDataRandomized()


    fun getQuestionsForQuizRandomized(quizID: String): List<Question>{
        return quizDao.getQuestionsForQuizRandomized(quizID)
    }

    fun getAnswersForQuestion(questionID: Int): List<Answer> {
        return quizDao.getAnswersForQuestion(questionID)
    }

    fun getQuestionsForQuiz(quizID: String): List<Question> {
        return quizDao.getQuestionsForQuiz(quizID)
    }

    fun getALlAnswersFromQuestion(questionID: Int): LiveData<List<QuestionWithAnswers>> {
        return quizDao.getQuestionWithAnswers(questionID)
    }

    fun getALlQuestionsFromQuiz(quizID: String): LiveData<List<QuizWithQuestions>> {
        return quizDao.getQuizWithQuestions(quizID)
    }

    suspend fun addQuiz(quiz: Quiz) {
        quizDao.addQuiz(quiz)
    }

    suspend fun updateQuiz(quiz: Quiz) {
        quizDao.updateQuiz(quiz)
    }

    suspend fun deleteQuiz(quiz: Quiz) {
        quizDao.deleteQuiz(quiz)
    }

    suspend fun deleteAllQuizzes() {
        quizDao.deleteAllQuizzes()
    }

    suspend fun addQuestion(question: Question) {
        quizDao.addQuestion(question)
    }

    suspend fun updateQuestion(question: Question) {
        quizDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(question: Question) {
        quizDao.deleteQuestion(question)
    }

    suspend fun deleteAllQuestions() {
        quizDao.deleteAllQuestions()
    }

    suspend fun addAnswer(answer: Answer) {
        quizDao.addAnswer(answer)
    }

    suspend fun updateAnswer(answer: Answer) {
        quizDao.updateAnswer(answer)
    }

    suspend fun deleteAnswer(answer: Answer) {
        quizDao.deleteAnswer(answer)
    }

    suspend fun deleteAllAnswers() {
        quizDao.deleteAllAnswers()
    }
}