package com.example.quizappbypawel.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizappbypawel.model.Answer
import com.example.quizappbypawel.model.Question
import com.example.quizappbypawel.model.Quiz

/**
 * QuizDatabase abstract class used to define and create the instance of the quiz database
 */

@Database(
    entities = [Quiz::class, Question::class, Answer::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        /**
         * Creating if the QuizDatabase objects doesn't exist otherwise getting the instance of the QuizDatabase object
         */
        fun getDatabase(context: Context): QuizDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}