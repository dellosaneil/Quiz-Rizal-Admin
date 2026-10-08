package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.QuizEnvironment
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.getTotalQuestions
import com.thelazybattley.joserizalquizadmin.domain.model.release.BuildSummary
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseOverview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchReleaseOverviewUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(): ReleaseOverview = coroutineScope {
        val debug = async { repository.fetchQuizContent(environment = QuizEnvironment.DEBUG) }
        val release = async { repository.fetchQuizContent(environment = QuizEnvironment.RELEASE) }
        val debugBooks = debug.await()
        val releaseBooks = release.await()
        ReleaseOverview(
            debug = debugBooks.toBuildSummary(),
            release = releaseBooks.toBuildSummary(),
            changes = diff(debugBooks = debugBooks, releaseBooks = releaseBooks)
        )
    }

    private fun List<Quiz>.toBuildSummary() = BuildSummary(
        bookCount = size,
        questionCount = sumOf { it.chapters.getTotalQuestions() }
    )

    // Books match by id, chapters by number, and questions by their text within a chapter.
    private fun diff(debugBooks: List<Quiz>, releaseBooks: List<Quiz>): List<ReleaseChange> {
        val releaseById = releaseBooks.associateBy { it.id }
        val debugIds = debugBooks.map { it.id }.toSet()
        val bookChanges = debugBooks.sortedBy { it.title }.flatMap { debugBook ->
            val releaseBook = releaseById[debugBook.id]
                ?: return@flatMap listOf(
                    ReleaseChange.BookAdded(bookTitle = debugBook.title, chapterCount = debugBook.chapters.size)
                )
            val detailsEdited = debugBook.title != releaseBook.title ||
                debugBook.author != releaseBook.author ||
                debugBook.category != releaseBook.category
            listOfNotNull(
                ReleaseChange.BookDetailsEdited(bookTitle = debugBook.title).takeIf { detailsEdited }
            ) + diffChapters(
                bookTitle = debugBook.title,
                debugChapters = debugBook.chapters,
                releaseChapters = releaseBook.chapters
            )
        }
        val removedBooks = releaseBooks
            .filterNot { it.id in debugIds }
            .sortedBy { it.title }
            .map { ReleaseChange.BookRemoved(bookTitle = it.title) }
        return bookChanges + removedBooks
    }

    private fun diffChapters(
        bookTitle: String,
        debugChapters: List<Chapter>,
        releaseChapters: List<Chapter>
    ): List<ReleaseChange> {
        val releaseByNumber = releaseChapters.associateBy { it.chapterNumber }
        val debugNumbers = debugChapters.map { it.chapterNumber }.toSet()
        val chapterChanges = debugChapters.sortedBy { it.chapterNumber }.flatMap { debugChapter ->
            val releaseChapter = releaseByNumber[debugChapter.chapterNumber]
                ?: return@flatMap listOf(
                    ReleaseChange.ChapterAdded(
                        bookTitle = bookTitle,
                        chapterNumber = debugChapter.chapterNumber,
                        chapterName = debugChapter.chapterName,
                        questionCount = debugChapter.questions.size
                    )
                )
            val renamed = debugChapter.chapterName != releaseChapter.chapterName
            listOfNotNull(
                ReleaseChange.ChapterRenamed(
                    bookTitle = bookTitle,
                    chapterNumber = debugChapter.chapterNumber,
                    chapterName = debugChapter.chapterName
                ).takeIf { renamed }
            ) + diffQuestions(bookTitle = bookTitle, debugChapter = debugChapter, releaseChapter = releaseChapter)
        }
        val removedChapters = releaseChapters
            .filterNot { it.chapterNumber in debugNumbers }
            .sortedBy { it.chapterNumber }
            .map {
                ReleaseChange.ChapterRemoved(
                    bookTitle = bookTitle,
                    chapterNumber = it.chapterNumber,
                    chapterName = it.chapterName
                )
            }
        return chapterChanges + removedChapters
    }

    private fun diffQuestions(
        bookTitle: String,
        debugChapter: Chapter,
        releaseChapter: Chapter
    ): List<ReleaseChange> {
        val chapterNumber = debugChapter.chapterNumber
        val releaseByText = releaseChapter.questions.associateBy { it.question }
        val debugTexts = debugChapter.questions.map { it.question }.toSet()
        val questionChanges = debugChapter.questions.mapNotNull { debugQuestion ->
            val releaseQuestion = releaseByText[debugQuestion.question]
                ?: return@mapNotNull ReleaseChange.QuestionAdded(
                    bookTitle = bookTitle,
                    chapterNumber = chapterNumber,
                    question = debugQuestion.question
                )
            val choicesChanged = debugQuestion.choices != releaseQuestion.choices
            val answerChanged = debugQuestion.answer != releaseQuestion.answer
            ReleaseChange.QuestionEdited(
                bookTitle = bookTitle,
                chapterNumber = chapterNumber,
                question = debugQuestion.question,
                choicesChanged = choicesChanged,
                answerChanged = answerChanged
            ).takeIf { choicesChanged || answerChanged }
        }
        val removedQuestions = releaseChapter.questions
            .filterNot { it.question in debugTexts }
            .map {
                ReleaseChange.QuestionRemoved(
                    bookTitle = bookTitle,
                    chapterNumber = chapterNumber,
                    question = it.question
                )
            }
        return questionChanges + removedQuestions
    }
}
