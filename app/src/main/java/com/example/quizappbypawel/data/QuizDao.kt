package com.example.quizappbypawel.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.quizappbypawel.model.*

/**
 * Data access object interface used to perform operations on QuizDatabase
 */

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuiz(quiz: Quiz)

    @Update
    suspend fun updateQuiz(quiz: Quiz)

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("DELETE FROM quiz_table")
    suspend fun deleteAllQuizzes()


    @Query("SELECT * FROM quiz_table ORDER BY quizID ASC")
    fun readAllData(): LiveData<MutableList<Quiz>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuestion(question: Question)

    @Update
    suspend fun updateQuestion(question: Question)

    @Delete
    suspend fun deleteQuestion(question: Question)

    @Query("DELETE FROM question_table")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM question_table ORDER BY question")
    fun readAllQuestionData(): LiveData<List<Question>>

    @Insert
    suspend fun addAnswer(answer: Answer)

    @Update
    suspend fun updateAnswer(answer: Answer)

    @Delete
    suspend fun deleteAnswer(answer: Answer)

    @Query("DELETE FROM answer_table")
    suspend fun deleteAllAnswers()

    @Query("SELECT * FROM answer_table ORDER BY answerID")
    fun readAllAnswerData(): LiveData<List<Answer>>

    @Query("SELECT * FROM answer_table ORDER BY RANDOM()")
    fun readAllAnswerDataRandomized(): LiveData<List<Answer>>

    @Transaction
    @Query("SELECT * FROM question_table WHERE questionID =:questionID")
    fun getQuestionWithAnswers(questionID: Int): LiveData<List<QuestionWithAnswers>>

    @Transaction
    @Query("SELECT * FROM quiz_table WHERE quizID =:quizID")
    fun getQuizWithQuestions(quizID: String): LiveData<List<QuizWithQuestions>>

    @Query("SELECT * FROM question_table WHERE quizID =:quizID")
    fun getQuestionsForQuiz(quizID: String): List<Question>

    @Query("SELECT * FROM question_table WHERE quizID =:quizID ORDER BY RANDOM()")
    fun getQuestionsForQuizRandomized(quizID: String): List<Question>

    @Query("SELECT * FROM answer_table WHERE questionID =:questionID")
    fun getAnswersForQuestion(questionID: Int): List<Answer>

    @Query("DELETE FROM question_table WHERE question =:question")
    suspend fun deleteQuestionFromQuiz(question: String)


}