package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val setBookUseCase: SetBookUseCase
) : BaseViewModel<AddBookState, AddBookActions>(), AddBookCallback {

    init {
        initState(initState = MutableStateFlow(value = AddBookState()))
    }

    override fun handleAction(action: AddBookActions) {
        when (action) {
            is AddBookActions.AddBook -> {
                setBookUseCase(
                    author = action.author,
                    bookName = action.bookName,
                    category = action.category
                )
            }
        }
    }
}
