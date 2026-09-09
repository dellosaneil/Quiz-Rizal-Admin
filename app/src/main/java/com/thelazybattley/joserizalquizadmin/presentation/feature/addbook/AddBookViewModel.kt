package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val setBookUseCase: SetBookUseCase
) : BaseViewModel<AddBookState, AddBookActions>(initialState = AddBookState()), AddBookCallback {

    override fun handleAction(action: AddBookActions) {
        when (action) {
            is AddBookActions.CategoryUpdated -> {
                updateState(newState = state.value.copy(category = action.category))
            }

            AddBookActions.PublishBook -> setBookUseCase(
                author = state.value.author,
                bookName = state.value.title,
                category = state.value.category.name
            )
            is AddBookActions.TextFieldUpdated -> {
                when (action.type) {
                    AddBookTextFieldTypes.TITLE -> updateState(
                        newState = state.value.copy(
                            title = action.text,
                            isButtonEnabled = action.text.isNotEmpty() && state.value.author.isNotEmpty()
                        )
                    )
                    AddBookTextFieldTypes.AUTHOR -> updateState(
                        newState = state.value.copy(
                            author = action.text,
                            isButtonEnabled = action.text.isNotEmpty() && state.value.title.isNotEmpty()
                        )
                    )
                }
            }
        }
    }
}
