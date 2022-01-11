package com.example.quizappbypawel.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class that create one to many relation between Question and Answer objects
 */
data class QuestionWithAnswers(
    @Embedded val question: Question,
    @Relation(
        parentColumn = "questionID",
        entityColumn = "questionID"
    )
    val answers: List<Answer>
)