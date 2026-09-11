package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchQuizContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val fetchQuizContentUseCase: FetchQuizContentUseCase
) :
    BaseViewModel<ContentState, ContentActions>(initialState = ContentState()), ContentCallback {
    override fun handleAction(action: ContentActions) {
        TODO("Not yet implemented")
    }
}
