package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChapterQuestionsViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    private val fetchReportedQuestionsUseCase: FetchReportedQuestionsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ChapterQuestionsState, ChapterQuestionsActions>(
    initialState = ChapterQuestionsState(
        quizId = savedStateHandle.get<String>(QUIZ_ID) ?: throw IllegalStateException("Quiz Id not found"),
        chapterNumber = savedStateHandle.get<Int>(CHAPTER_NUMBER)
            ?: throw IllegalStateException("Chapter Number not found")
    )
), ChapterQuestionsCallback {

    init {
        viewModelScope.launch {
            getQuizByIdUseCase(id = state.value.quizId).collect { quiz ->
                updateState(newState = state.value.copy(quiz = quiz))
            }
        }
    }

    override fun handleAction(action: ChapterQuestionsActions) {
        when (action) {
            is ChapterQuestionsActions.Navigate -> updateState(
                newState = state.value.copy(destination = action.destination)
            )

            ChapterQuestionsActions.RefreshReports -> fetchReportCounts()
        }
    }

    // Report tags are a nice-to-have; the list still works if this fails.
    private fun fetchReportCounts() {
        viewModelScope.launch {
            runCatching { withContext(context = Dispatchers.IO) { fetchReportedQuestionsUseCase() } }
                .onSuccess { reports ->
                    updateState(
                        newState = state.value.copy(
                            reportCounts = reports
                                .filter { it.quizId == state.value.quizId && it.chapterNumber == state.value.chapterNumber }
                                .associate { it.question to it.reportCount }
                        )
                    )
                }
        }
    }
}
