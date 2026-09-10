package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.google.firebase.firestore.FirebaseFirestore
import com.thelazybattley.joserizalquizadmin.BuildConfig
import com.thelazybattley.joserizalquizadmin.util.Constants
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class SetBookUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    ) {
        val variant = BuildConfig.BUILD_TYPE
        val bookDetail = mutableMapOf<String, Any>()

        val docRef = firestore
            .collection(Constants.QUIZ)
            .document(variant)
            .collection(Constants.BOOKS)
            .document()

        bookDetail[Constants.ID] = docRef.id
        bookDetail[Constants.AUTHOR] = author
        bookDetail[Constants.BOOK_NAME] = bookName
        bookDetail[Constants.CATEGORY] = category

        val chaptersJson = JSONArray().apply {
            chapters.forEachIndexed { index, name ->
                put(JSONObject().apply {
                    put(Constants.CHAPTER_NAME, name)
                    put(Constants.QUESTIONS, JSONArray())
                    put(Constants.CHAPTER_NUMBER, index + 1)
                })
            }
        }.toString()

        bookDetail[Constants.CHAPTERS] = chaptersJson
        docRef.set(bookDetail)
    }

}
