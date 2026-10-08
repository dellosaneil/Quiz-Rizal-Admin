package com.thelazybattley.joserizalquizadmin.data

import com.google.firebase.firestore.FirebaseFirestore
import com.thelazybattley.joserizalquizadmin.BuildConfig
import com.thelazybattley.joserizalquizadmin.data.local.dao.QuizDao
import com.thelazybattley.joserizalquizadmin.data.local.entity.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.ChapterDto
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.QuizDto
import com.thelazybattley.joserizalquizadmin.data.network.model.quiz.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions.ReportedQuestionDto
import com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions.toDomain
import com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks.SuggestedBookDto
import com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks.toDomain
import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.toEntity
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
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
    override suspend fun fetchQuizContent(): List<Quiz> {
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
            QuizDto(
                author = author,
                bookName = bookName,
                chapters = chapters,
                category = category,
                id = id
            ).toDomain()
        }
    }

    override fun setQuiz(
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
        docRef.set(bookDetail)
        return docRef.id
    }

    override fun setUpdatedQuiz(quiz: Quiz) {
        val variant = BuildConfig.BUILD_TYPE
        val bookDetail = mutableMapOf<String, Any>()

        val docRef = firestore
            .collection(QUIZ)
            .document(variant)
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
        docRef.set(bookDetail)
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

    // The quiz app appends each submission to a JSON array stored in the "feedback" string field.
    private suspend fun <T> fetchFeedback(document: String, serializer: KSerializer<T>): List<T> {
        val snapshot = firestore
            .collection(QUIZ)
            .document(BuildConfig.BUILD_TYPE)
            .collection(FEEDBACK)
            .document(document)
            .get()
            .await()
        val feedbackJson = snapshot.getString(FEEDBACK) ?: return emptyList()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.decodeFromString(ListSerializer(serializer), feedbackJson)
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

        debugBooks.documents.chunked(FIRESTORE_BATCH_LIMIT).forEach { documents ->
            val batch = firestore.batch()
            documents.forEach { document ->
                val data = document.data ?: return@forEach
                batch.set(releaseBooks.document(document.id), data)
            }
            batch.commit().await()
        }
    }
}
