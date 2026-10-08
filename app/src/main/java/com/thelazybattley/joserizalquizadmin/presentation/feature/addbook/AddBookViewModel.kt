package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.usecase.InsertQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetQuizUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.AUTHOR
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.BOOK_TITLE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val setQuizUseCase: SetQuizUseCase,
    private val insertQuizUseCase: InsertQuizUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AddBookState, AddBookActions>(
    initialState = savedStateHandle.let { handle ->
        val title = handle.get<String>(BOOK_TITLE).orEmpty()
        val author = handle.get<String>(AUTHOR).orEmpty()
        AddBookState(
            title = title,
            author = author,
            isFromSuggestion = title.isNotBlank() || author.isNotBlank()
        )
    }
), AddBookCallback {

    override fun handleAction(action: AddBookActions) {
        if (action is AddBookActions.NavigateDestination) {
            updateState(newState = state.value.copy(destination = action.destination))
            return
        }
        // The form is locked while publishing and after the book is published.
        if (state.value.publishPhase != PublishPhase.EDITING) return

        when (action) {
            is AddBookActions.CategoryUpdated -> {
                updateState(newState = state.value.copy(category = action.category))
            }

            AddBookActions.PublishBook -> publish()

            is AddBookActions.TextFieldUpdated -> updateState(
                newState = when (action.type) {
                    AddBookTextFieldTypes.TITLE -> state.value.copy(title = action.text)
                    AddBookTextFieldTypes.AUTHOR -> state.value.copy(author = action.text)
                }
            )

            is AddBookActions.Chapter -> {
                val chapters = state.value.chapters
                val updatedChapters = when (action) {
                    AddBookActions.Chapter.Add -> chapters + ""
                    // A book always keeps at least one chapter.
                    is AddBookActions.Chapter.Delete -> if (chapters.size > 1) {
                        chapters.filterIndexed { index, _ -> index != action.index }
                    } else {
                        chapters
                    }

                    is AddBookActions.Chapter.Update -> chapters.mapIndexed { index, chapter ->
                        if (index == action.index) action.text else chapter
                    }
                }
                updateState(newState = state.value.copy(chapters = updatedChapters))
            }

            is AddBookActions.NavigateDestination -> Unit
        }
    }

    private fun publish() {
        val current = state.value
        if (current.blocker != null) return
        val title = current.title.trim()
        val author = current.author.trim()
        val chapters = current.chapters.map { it.trim() }
        updateState(newState = current.copy(publishPhase = PublishPhase.PUBLISHING, publishFailed = false))
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    val quizId = setQuizUseCase(
                        author = author,
                        bookName = title,
                        category = current.category.name,
                        chapters = chapters
                    )
                    insertQuizUseCase(
                        quiz = listOf(
                            Quiz(
                                id = quizId,
                                title = title,
                                author = author,
                                category = current.category,
                                chapters = chapters.mapIndexed { index, chapter ->
                                    Chapter(
                                        chapterName = chapter,
                                        questions = emptyList(),
                                        chapterNumber = index.inc()
                                    )
                                }
                            )
                        )
                    )
                    quizId
                }
            }
                .onSuccess { quizId ->
                    updateState(newState = state.value.copy(publishPhase = PublishPhase.Published(quizId = quizId)))
                }
                .onFailure {
                    updateState(newState = state.value.copy(publishPhase = PublishPhase.EDITING, publishFailed = true))
                }
        }
    }
}
