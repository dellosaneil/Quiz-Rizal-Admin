package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.QuizEnvironment
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

// Undoes one question change in Debug so it matches Release again. Release itself is never touched.
class RevertReleaseChangeUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(change: ReleaseChange) = coroutineScope {
        // Read both environments fresh, so the revert applies to what's there now, not what the list showed.
        val debug = async { repository.fetchQuizContent(environment = QuizEnvironment.DEBUG) }
        val release = async { repository.fetchQuizContent(environment = QuizEnvironment.RELEASE) }
        val (bookId, chapterNumber, questionText) = when (change) {
            is ReleaseChange.QuestionAdded -> Triple(change.bookId, change.chapterNumber, change.question)
            is ReleaseChange.QuestionEdited -> Triple(change.bookId, change.chapterNumber, change.question)
            is ReleaseChange.QuestionRemoved -> Triple(change.bookId, change.chapterNumber, change.question)
            else -> throw IllegalArgumentException("Only question changes can be reverted")
        }
        val debugBook = debug.await().firstOrNull { it.id == bookId }
            ?: throw IllegalStateException("Book is no longer in Debug")
        val releaseQuestions = release.await()
            .firstOrNull { it.id == bookId }
            ?.chapters
            ?.firstOrNull { it.chapterNumber == chapterNumber }
            ?.questions
            .orEmpty()

        val updatedBook = debugBook.copy(
            chapters = debugBook.chapters.map { chapter ->
                if (chapter.chapterNumber != chapterNumber) return@map chapter
                chapter.copy(
                    questions = revertQuestions(
                        change = change,
                        debugQuestions = chapter.questions,
                        releaseQuestions = releaseQuestions,
                        questionText = questionText
                    )
                )
            }
        )
        repository.saveQuiz(quiz = updatedBook, environment = QuizEnvironment.DEBUG)
    }

    private fun revertQuestions(
        change: ReleaseChange,
        debugQuestions: List<Question>,
        releaseQuestions: List<Question>,
        questionText: String
    ): List<Question> {
        val releaseIndex = releaseQuestions.indexOfFirst { it.question == questionText }
        return when (change) {
            is ReleaseChange.QuestionAdded -> debugQuestions.filterNot { it.question == questionText }

            is ReleaseChange.QuestionEdited -> {
                val releaseQuestion = releaseQuestions.getOrNull(releaseIndex)
                    ?: throw IllegalStateException("Question is no longer in Release")
                debugQuestions.map { if (it.question == questionText) releaseQuestion else it }
            }

            // Put it back where it sits in Release, as close as the current Debug order allows.
            is ReleaseChange.QuestionRemoved -> {
                val releaseQuestion = releaseQuestions.getOrNull(releaseIndex)
                    ?: throw IllegalStateException("Question is no longer in Release")
                if (debugQuestions.any { it.question == questionText }) return debugQuestions
                debugQuestions.toMutableList().apply {
                    add(releaseIndex.coerceIn(minimumValue = 0, maximumValue = size), releaseQuestion)
                }
            }

            else -> debugQuestions
        }
    }
}
