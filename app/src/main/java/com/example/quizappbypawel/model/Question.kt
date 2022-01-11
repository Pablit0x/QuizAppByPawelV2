package com.example.quizappbypawel.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents the question_table
 * This class is extending Parcelable so we are able to pass objects of that class as a argument while navigating to the other fragments
 */
@Parcelize
@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val questionID: Int = 0,
    val question: String,
    val quizID: String
) : Parcelable