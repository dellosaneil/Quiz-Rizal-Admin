package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion

data class EditQuestionState(
    val quizId: String = "",
    val chapterNumber: Int = -1,
    // The question's text when the screen opened; it identifies the question within the chapter.
    val originalText: String = "",
    val quiz: Quiz? = null,
    val questionIndex: Int = -1,
    val original: Question? = null,
    val isNotFound: Boolean = false,
    val question: String = "",
    val choices: List<String> = emptyList(),
    val correctAnswerIndex: Int = -1,
    val report: RankedReportedQuestion? = null,
    val clearReports: Boolean = true,
    // Bumped by Undo changes so the text fields reload the original values.
    val formVersion: Int = 0,
    val isSaving: Boolean = false,
    val saveFailed: Boolean = false,
    val showDeleteSheet: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteFailed: Boolean = false,
    val destination: EditQuestionDestinations? = null
) : BaseState {

    val chapter: Chapter?
        get() = quiz?.chapters?.firstOrNull { it.chapterNumber == chapterNumber }

    val originalAnswerIndex: Int
        get() = original?.let { it.choices.indexOf(it.answer) } ?: -1

    val isQuestionEdited: Boolean
        get() = original != null && question != original.question

    val editedChoiceIndices: Set<Int>
        get() = choices.indices.filter { choices[it] != original?.choices?.getOrNull(it) }.toSet()

    val isAnswerMoved: Boolean
        get() = original != null && correctAnswerIndex != originalAnswerIndex

    val hasChanges: Boolean
        get() = isQuestionEdited || editedChoiceIndices.isNotEmpty() || isAnswerMoved

    val duplicateChoiceIndices: Set<Int>
        get() {
            val normalized = choices.map { it.trim().lowercase() }
            return normalized.indices.filter { index ->
                normalized[index].isNotEmpty() && normalized.count { it == normalized[index] } > 1
            }.toSet()
        }

    val blankChoiceCount: Int
        get() = choices.count { it.isBlank() }

    val blocker: EditBlocker?
        get() = when {
            question.isBlank() -> EditBlocker.MISSING_QUESTION
            blankChoiceCount > 0 -> EditBlocker.BLANK_CHOICES
            duplicateChoiceIndices.isNotEmpty() -> EditBlocker.DUPLICATE_CHOICES
            correctAnswerIndex !in choices.indices -> EditBlocker.NO_ANSWER
            !hasChanges -> EditBlocker.NO_CHANGES
            else -> null
        }

    val isBusy: Boolean
        get() = isSaving || isDeleting
}

enum class EditBlocker {
    MISSING_QUESTION,
    BLANK_CHOICES,
    DUPLICATE_CHOICES,
    NO_ANSWER,
    NO_CHANGES
}
