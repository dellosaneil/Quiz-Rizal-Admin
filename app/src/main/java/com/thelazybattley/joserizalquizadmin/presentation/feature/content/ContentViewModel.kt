package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchQuizContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val fetchQuizContentUseCase: FetchQuizContentUseCase
) :
    BaseViewModel<ContentState, ContentActions>(initialState = ContentState()), ContentCallback {

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            updateState(
                newState = state.value.copy(
                    quiz = fetchQuizContentUseCase(),
                    isLoading = false,
                )
            )
        }
    }

    override fun handleAction(action: ContentActions) {
        TODO("Not yet implemented")
    }
}
