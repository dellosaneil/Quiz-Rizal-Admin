package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz

data class AddQuestionState(
    val quizId: String = "",
    val chapterNumber: Int = -1,
    val quiz: Quiz? = null,
    val question: String = "",
    val choices: List<String> = List(size = CHOICE_COUNT) { "" },
    val correctAnswerIndex: Int = -1,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
    val lastSaved: SavedQuestion? = null,
    // Bumped after each save so the text fields start empty again.
    val formVersion: Int = 0,
    val savedCount: Int = 0,
    val destination: AddQuestionDestinations? = null
) : BaseState {

    val chapter: Chapter?
        get() = quiz?.chapters?.firstOrNull { it.chapterNumber == chapterNumber }

    // Indexes of filled-in choices whose text matches another choice, ignoring case and spacing.
    val duplicateChoiceIndices: Set<Int>
        get() {
            val normalized = choices.map { it.trim().lowercase() }
            return normalized.indices.filter { index ->
                normalized[index].isNotEmpty() && normalized.count { it == normalized[index] } > 1
            }.toSet()
        }

    val blankChoiceCount: Int
        get() = choices.count { it.isBlank() }

    // What still stops the question from being saved, in the order the admin fills the form.
    val blocker: SaveBlocker?
        get() = when {
            question.isBlank() -> SaveBlocker.MISSING_QUESTION
            blankChoiceCount > 0 -> SaveBlocker.BLANK_CHOICES
            duplicateChoiceIndices.isNotEmpty() -> SaveBlocker.DUPLICATE_CHOICES
            correctAnswerIndex !in choices.indices -> SaveBlocker.NO_ANSWER
            else -> null
        }

    companion object {
        const val CHOICE_COUNT = 4
    }
}

enum class SaveBlocker {
    MISSING_QUESTION,
    BLANK_CHOICES,
    DUPLICATE_CHOICES,
    NO_ANSWER
}

data class SavedQuestion(val number: Int, val question: String)
