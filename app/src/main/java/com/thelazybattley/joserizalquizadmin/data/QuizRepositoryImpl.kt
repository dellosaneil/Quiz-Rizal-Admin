package com.thelazybattley.joserizalquizadmin.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.thelazybattley.joserizalquizadmin.BuildConfig
import com.thelazybattley.joserizalquizadmin.data.local.dao.QuizDao
import com.thelazybattley.joserizalquizadmin.data.local.entity.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.ChapterDto
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.QuizDto
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions.ReportedQuestionDto
import com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions.toDto
import com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks.SuggestedBookDto
import com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks.toDto
import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.QuizEnvironment
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.toEntity
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.toSuggestionKey
import com.thelazybattley.joserizalquizadmin.util.Constants
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.AUTHOR
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.BOOKS
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.BOOK_NAME
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.CATEGORY
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.CHAPTERS
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.DEBUG
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.DISPUTE_ANSWER
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.FEEDBACK
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.FIRESTORE_BATCH_LIMIT
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.ID
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.QUIZ
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.RELEASE
import com.thelazybattley.joserizalquizadmin.util.Constants.Companion.SUGGESTED_BOOKS
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: QuizDao
) : QuizRepository {
    override suspend fun fetchQuizContent(): List<Quiz> = fetchBooks(environmentPath = BuildConfig.BUILD_TYPE)

    override suspend fun fetchQuizContent(environment: QuizEnvironment): List<Quiz> = fetchBooks(
        environmentPath = environment.path()
    )

    private suspend fun fetchBooks(environmentPath: String): List<Quiz> {
        val books = firestore
            .collection(QUIZ)
            .document(environmentPath)
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
            QuizDto(
                author = author,
                bookName = bookName,
                chapters = chapters,
                category = category,
                id = id
            ).toDomain()
        }
    }

    override suspend fun setQuiz(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    ): String {
        val variant = BuildConfig.BUILD_TYPE
        val bookDetail = mutableMapOf<String, Any>()

        val docRef = firestore
            .collection(QUIZ)
            .document(variant)
            .collection(BOOKS)
            .document()

        bookDetail[ID] = docRef.id
        bookDetail[AUTHOR] = author
        bookDetail[BOOK_NAME] = bookName
        bookDetail[CATEGORY] = category

        val chaptersJson = JSONArray().apply {
            chapters.forEachIndexed { index, name ->
                put(JSONObject().apply {
                    put(Constants.CHAPTER_NAME, name)
                    put(Constants.QUESTIONS, JSONArray())
                    put(Constants.CHAPTER_NUMBER, index + 1)
                })
            }
        }.toString()
        bookDetail[CHAPTERS] = chaptersJson
        docRef.set(bookDetail).await()
        return docRef.id
    }

    override suspend fun setUpdatedQuiz(quiz: Quiz) = writeBook(quiz = quiz, environmentPath = BuildConfig.BUILD_TYPE)

    override suspend fun saveQuiz(quiz: Quiz, environment: QuizEnvironment) {
        val environmentPath = environment.path()
        writeBook(quiz = quiz, environmentPath = environmentPath)
        // The local copy mirrors this build's environment only.
        if (environmentPath == BuildConfig.BUILD_TYPE) {
            dao.insertAllQuiz(quiz = listOf(quiz).toEntity())
        }
    }

    private fun QuizEnvironment.path() = when (this) {
        QuizEnvironment.DEBUG -> DEBUG
        QuizEnvironment.RELEASE -> RELEASE
    }

    private suspend fun writeBook(quiz: Quiz, environmentPath: String) {
        val bookDetail = mutableMapOf<String, Any>()

        val docRef = firestore
            .collection(QUIZ)
            .document(environmentPath)
            .collection(BOOKS)
            .document(quiz.id)

        bookDetail[ID] = quiz.id
        bookDetail[AUTHOR] = quiz.author
        bookDetail[BOOK_NAME] = quiz.title
        bookDetail[CATEGORY] = quiz.category.name

        val chaptersJson = JSONArray().apply {
            quiz.chapters.forEach { chapter ->
                put(JSONObject().apply {
                    put(Constants.CHAPTER_NAME, chapter.chapterName)
                    put(Constants.CHAPTER_NUMBER, chapter.chapterNumber)
                    put(Constants.QUESTIONS, JSONArray().apply {
                        chapter.questions.forEach { question ->
                            put(JSONObject().apply {
                                put(Constants.QUESTION, question.question)
                                put(Constants.CHOICES, JSONArray(question.choices))
                                put(Constants.ANSWER, question.answer)
                            })
                        }
                    })
                })
            }
        }.toString()
        bookDetail[CHAPTERS] = chaptersJson
        docRef.set(bookDetail).await()
    }

    override fun getAllQuiz() = dao.getAllQuiz().map { entity ->
        entity.map { it.toDomain() }
    }

    override suspend fun insertQuiz(quiz: List<Quiz>) =
        dao.insertAllQuiz(quiz = quiz.toEntity())

    override fun getQuizById(id: String) = dao.getQuizById(id = id).map { it.toDomain() }

    override suspend fun fetchSuggestedBooks(): List<SuggestedBook> = fetchFeedback(
        document = SUGGESTED_BOOKS,
        serializer = SuggestedBookDto.serializer()
    ).map { it.toDomain() }

    override suspend fun fetchReportedQuestions(): List<ReportedQuestion> = fetchFeedback(
        document = DISPUTE_ANSWER,
        serializer = ReportedQuestionDto.serializer()
    ).map { it.toDomain() }

    override suspend fun removeSuggestedBook(bookTitle: String): List<SuggestedBook> {
        val key = bookTitle.toSuggestionKey()
        return updateFeedback(
            document = SUGGESTED_BOOKS,
            serializer = SuggestedBookDto.serializer()
        ) { suggestions ->
            val (removed, kept) = suggestions.partition { it.bookTitle.toSuggestionKey() == key }
            kept to removed.map { it.toDomain() }
        }
    }

    override suspend fun restoreSuggestedBooks(suggestedBooks: List<SuggestedBook>) {
        updateFeedback(
            document = SUGGESTED_BOOKS,
            serializer = SuggestedBookDto.serializer()
        ) { suggestions ->
            suggestions + suggestedBooks.map { it.toDto() } to Unit
        }
    }

    override suspend fun removeReportedQuestion(
        quizId: String,
        chapterNumber: Int,
        question: String
    ): List<ReportedQuestion> = updateFeedback(
        document = DISPUTE_ANSWER,
        serializer = ReportedQuestionDto.serializer()
    ) { reports ->
        val (removed, kept) = reports.partition {
            it.quizId == quizId && it.chapterNumber == chapterNumber && it.question == question
        }
        kept to removed.map { it.toDomain() }
    }

    override suspend fun restoreReportedQuestions(reportedQuestions: List<ReportedQuestion>) {
        updateFeedback(
            document = DISPUTE_ANSWER,
            serializer = ReportedQuestionDto.serializer()
        ) { reports ->
            reports + reportedQuestions.map { it.toDto() } to Unit
        }
    }

    private val feedbackJson = Json {
        ignoreUnknownKeys = true
        // Match the quiz app, which writes every key on every entry.
        encodeDefaults = true
    }

    private fun feedbackDocument(document: String) = firestore
        .collection(QUIZ)
        .document(BuildConfig.BUILD_TYPE)
        .collection(FEEDBACK)
        .document(document)

    // The quiz app appends each submission to a JSON array stored in the "feedback" string field.
    private suspend fun <T> fetchFeedback(document: String, serializer: KSerializer<T>): List<T> {
        val snapshot = feedbackDocument(document = document).get().await()
        val json = snapshot.getString(FEEDBACK) ?: return emptyList()
        return feedbackJson.decodeFromString(ListSerializer(serializer), json)
    }

    // Rewrites the feedback array in a transaction so submissions made meanwhile aren't lost.
    private suspend fun <T, R> updateFeedback(
        document: String,
        serializer: KSerializer<T>,
        transform: (List<T>) -> Pair<List<T>, R>
    ): R {
        val documentRef = feedbackDocument(document = document)
        val listSerializer = ListSerializer(serializer)
        return firestore.runTransaction { transaction ->
            val existing = transaction.get(documentRef).getString(FEEDBACK)
                ?.let { feedbackJson.decodeFromString(listSerializer, it) }
                .orEmpty()
            val (updated, result) = transform(existing)
            transaction.set(
                documentRef,
                mapOf(FEEDBACK to feedbackJson.encodeToString(listSerializer, updated)),
                SetOptions.merge()
            )
            result
        }.await()
    }

    override suspend fun setQuizContentToRelease() {
        val debugBooks = firestore
            .collection(QUIZ)
            .document(DEBUG)
            .collection(BOOKS)
            .get()
            .await()

        val releaseBooks = firestore
            .collection(QUIZ)
            .document(RELEASE)
            .collection(BOOKS)

        val debugIds = debugBooks.documents.map { it.id }.toSet()
        val removedIds = releaseBooks.get().await().documents
            .map { it.id }
            .filterNot { it in debugIds }

        // One write per book: copy each debug book, then delete release books removed from debug.
        val writes = debugBooks.documents.mapNotNull { document ->
            document.data?.let { data -> { batch: WriteBatch -> batch.set(releaseBooks.document(document.id), data) } }
        } + removedIds.map { id -> { batch: WriteBatch -> batch.delete(releaseBooks.document(id)) } }

        writes.chunked(FIRESTORE_BATCH_LIMIT).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { write -> write(batch) }
            batch.commit().await()
        }
    }
}
