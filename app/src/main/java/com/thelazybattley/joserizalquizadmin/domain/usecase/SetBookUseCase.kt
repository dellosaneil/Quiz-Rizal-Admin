package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.google.firebase.firestore.FirebaseFirestore
import com.thelazybattley.joserizalquizadmin.BuildConfig
import com.thelazybattley.joserizalquizadmin.util.Constants
import javax.inject.Inject

class SetBookUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(
        author: String,
        bookName: String,
        category: String
    ) {
        val variant = BuildConfig.BUILD_TYPE
        val bookDetail = mutableMapOf<String, String>()

        val docRef = firestore
            .collection(Constants.QUIZ)
            .document(variant)
            .collection(Constants.BOOKS)
            .document()

        bookDetail[Constants.ID] = docRef.id
        bookDetail[Constants.AUTHOR] = author
        bookDetail[Constants.BOOK_NAME] = bookName
        bookDetail[Constants.CATEGORY] = category
        docRef.set(bookDetail)
    }

}
