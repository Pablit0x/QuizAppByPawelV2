package com.example.quizappbypawel.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents the quiz_table
 * This class is extending Parcelable so we are able to pass objects of that class as a argument while navigating to the other fragments
 */
@Parcelize
@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey(autoGenerate = false)
    val quizID: String,
    val quizName: String
) : Parcelable