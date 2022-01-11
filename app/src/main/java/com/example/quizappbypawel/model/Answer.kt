package com.example.quizappbypawel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents the answer_table
 */
@Entity(tableName = "answer_table")
data class Answer(
    @PrimaryKey(autoGenerate = true)
    val answerID: Int = 0,
    val answer: String,
    val isCorrect: Boolean,
    val questionID: Int
)