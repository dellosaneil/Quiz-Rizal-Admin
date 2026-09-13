package com.thelazybattley.joserizalquizadmin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thelazybattley.joserizalquizadmin.data.local.entity.QuizEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT * FROM quizentity")
    fun getAllQuiz(): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuiz(quiz: List<QuizEntity>)

    @Query("SELECT * FROM quizentity WHERE id = :id")
    fun getQuizById(id: String): Flow<QuizEntity>

}
