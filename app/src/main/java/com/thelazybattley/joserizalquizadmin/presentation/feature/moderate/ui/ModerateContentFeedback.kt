package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

enum class ModerateContentFeedback(
    @StringRes val id: Int
) {
    SUGGESTED_BOOKS(
        id = R.string.suggested_books
    ), REPORTED_QUESTIONS(
        id = R.string.reported_questions
    )
}
