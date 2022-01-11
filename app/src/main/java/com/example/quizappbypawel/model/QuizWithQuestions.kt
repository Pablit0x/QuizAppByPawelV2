package com.example.quizappbypawel.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class that create one to many relation between Quiz and Question objects
 */
data class QuizWithQuestions(
    @Embedded val quizID: Quiz,
    @Relation(
        parentColumn = "quizID",
        entityColumn = "quizID"
    )
    val questions: List<Question>
)