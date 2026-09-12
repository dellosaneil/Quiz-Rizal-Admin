package com.thelazybattley.joserizalquizadmin.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thelazybattley.joserizalquizadmin.data.local.dao.QuizDao

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val quizDao: QuizDao

    companion object {
        const val DATABASE_NAME = "quiz_db"
    }
}
