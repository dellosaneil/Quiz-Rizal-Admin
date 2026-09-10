package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ContentViewModel: BaseViewModel<ContentState, ContentActions>(initialState = ContentState()), ContentCallback {
    override fun handleAction(action: ContentActions) {
        TODO("Not yet implemented")
    }
}
