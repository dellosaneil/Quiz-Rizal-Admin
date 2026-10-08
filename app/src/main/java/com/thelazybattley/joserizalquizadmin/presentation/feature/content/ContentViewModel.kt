package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchQuizContentUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetAllQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.InsertQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val fetchQuizContentUseCase: FetchQuizContentUseCase,
    private val getAllQuizUseCase: GetAllQuizUseCase,
    private val insertQuizUseCase: InsertQuizUseCase
) :
    BaseViewModel<ContentState, ContentActions>(initialState = ContentState()), ContentCallback {

    init {
        viewModelScope.launch {
            // The cached library still shows if the refresh fails.
            runCatching {
                withContext(context = Dispatchers.IO) { insertQuizUseCase(quiz = fetchQuizContentUseCase()) }
            }
            updateState(newState = state.value.copy(isLoading = false))
        }
        viewModelScope.launch {
            getAllQuizUseCase().collect { quiz ->
                updateState(
                    newState = state.value.copy(
                        quiz = quiz,
                        // Show cached books right away instead of waiting for the refresh.
                        isLoading = state.value.isLoading && quiz.isEmpty()
                    )
                )
            }
        }
    }

    override fun handleAction(action: ContentActions) {
        when (action) {
            is ContentActions.ExpandBook -> updateState(newState = state.value.copy(expandedBook = action.id))
            is ContentActions.Navigate -> updateState(newState = state.value.copy(destination = action.destination))
            is ContentActions.FilterSelected -> updateState(newState = state.value.copy(categoryFilter = action.category))
        }
    }
}
