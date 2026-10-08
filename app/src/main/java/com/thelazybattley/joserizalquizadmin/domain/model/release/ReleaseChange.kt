package com.thelazybattley.joserizalquizadmin.domain.model.release

enum class ReleaseChangeKind {
    NEW,
    EDITED,
    REMOVED
}

// A difference between the debug and release copies of the quiz content.
sealed interface ReleaseChange {
    val kind: ReleaseChangeKind

    data class BookAdded(val bookTitle: String, val chapterCount: Int) : ReleaseChange {
        override val kind = ReleaseChangeKind.NEW
    }

    data class BookDetailsEdited(val bookTitle: String) : ReleaseChange {
        override val kind = ReleaseChangeKind.EDITED
    }

    data class BookRemoved(val bookTitle: String) : ReleaseChange {
        override val kind = ReleaseChangeKind.REMOVED
    }

    data class ChapterAdded(
        val bookTitle: String,
        val chapterNumber: Int,
        val chapterName: String,
        val questionCount: Int
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.NEW
    }

    data class ChapterRenamed(
        val bookTitle: String,
        val chapterNumber: Int,
        val chapterName: String
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.EDITED
    }

    data class ChapterRemoved(
        val bookTitle: String,
        val chapterNumber: Int,
        val chapterName: String
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.REMOVED
    }

    data class QuestionAdded(
        val bookId: String,
        val bookTitle: String,
        val chapterNumber: Int,
        val question: String
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.NEW
    }

    data class QuestionEdited(
        val bookId: String,
        val bookTitle: String,
        val chapterNumber: Int,
        val question: String,
        val choicesChanged: Boolean,
        val answerChanged: Boolean
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.EDITED
    }

    data class QuestionRemoved(
        val bookId: String,
        val bookTitle: String,
        val chapterNumber: Int,
        val question: String
    ) : ReleaseChange {
        override val kind = ReleaseChangeKind.REMOVED
    }
}

// Question-level changes can be reverted from the Release tab, putting Debug back to match Release.
val ReleaseChange.isRevertible: Boolean
    get() = this is ReleaseChange.QuestionAdded ||
            this is ReleaseChange.QuestionEdited ||
            this is ReleaseChange.QuestionRemoved
