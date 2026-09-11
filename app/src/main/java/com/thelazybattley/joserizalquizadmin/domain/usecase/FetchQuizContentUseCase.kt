package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.google.firebase.firestore.FirebaseFirestore
import com.thelazybattley.joserizalquizadmin.BuildConfig
import com.thelazybattley.joserizalquizadmin.data.model.quiz.BookQuizDto
import com.thelazybattley.joserizalquizadmin.data.model.quiz.ChapterDto
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.AUTHOR
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.BOOKS
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.BOOK_NAME
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.CATEGORY
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.CHAPTERS
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.ID
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.QUIZ
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json


class FetchQuizContentUseCase(private val firestore: FirebaseFirestore) {

    suspend operator fun invoke(): List<BookQuizDto> {
        val books = firestore
            .collection(QUIZ)
            .document(BuildConfig.BUILD_TYPE)
            .collection(BOOKS)
            .get()
            .await()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return books.documents.map {
            val author = it.getString(AUTHOR) ?: ""
            val id = it.getString(ID) ?: ""
            val bookName = it.getString(BOOK_NAME) ?: ""
            val chaptersJson = it.getString(CHAPTERS) ?: "[]"
            val category = it.getString(CATEGORY) ?: ""
            val chapters = json.decodeFromString<List<ChapterDto>>(chaptersJson)
            BookQuizDto(
                author = author,
                bookName = bookName,
                chapters = chapters,
                category = category,
                id = id
            )
        }
    }
}

