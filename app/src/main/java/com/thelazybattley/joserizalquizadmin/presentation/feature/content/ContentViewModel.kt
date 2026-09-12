package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchQuizContentUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetAllQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.InsertQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val fetchQuizContentUseCase: FetchQuizContentUseCase,
    private val getAllQuizUseCase: GetAllQuizUseCase,
    private val insertQuizUseCase: InsertQuizUseCase
) :
    BaseViewModel<ContentState, ContentActions>(initialState = ContentState()), ContentCallback {

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            insertQuizUseCase(quiz = fetchQuizContentUseCase())
        }
        viewModelScope.launch(context = Dispatchers.IO) {
            getAllQuizUseCase().collectLatest { quiz ->
                updateState(
                    newState = state.value.copy(
                        quiz = quiz,
                        isLoading = false
                    )
                )
            }
        }
    }

    override fun handleAction(action: ContentActions) {
        when (action) {
            is ContentActions.ExpandBook -> updateState(newState = state.value.copy(expandedBook = action.id))
            is ContentActions.Navigate -> updateState(newState = state.value.copy(destination = action.destination))
        }
    }
}
