package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val setQuizUseCase: SetQuizUseCase
) : BaseViewModel<AddBookState, AddBookActions>(initialState = AddBookState()), AddBookCallback {

    override fun handleAction(action: AddBookActions) {
        when (action) {
            is AddBookActions.CategoryUpdated -> {
                updateState(newState = state.value.copy(category = action.category))
            }

            AddBookActions.PublishBook -> setQuizUseCase(
                author = state.value.author,
                bookName = state.value.title,
                category = state.value.category.name,
                chapters = state.value.chapters
            )

            is AddBookActions.TextFieldUpdated -> {
                when (action.type) {
                    AddBookTextFieldTypes.TITLE -> {
                        updateState(
                            newState = state.value.copy(
                                title = action.text,
                                isButtonEnabled = action.text.isNotEmpty() && state.value.author.isNotEmpty()
                            )
                        )
                        setButtonEnabled()
                    }

                    AddBookTextFieldTypes.AUTHOR -> {
                        updateState(
                            newState = state.value.copy(
                                author = action.text,
                                isButtonEnabled = action.text.isNotEmpty() && state.value.title.isNotEmpty()
                            )
                        )
                        setButtonEnabled()
                    }
                }
            }

            is AddBookActions.Chapter -> {
                when (action) {
                    is AddBookActions.Chapter.Add -> updateState(
                        newState = state.value.copy(
                            chapters = state.value.chapters + "",
                            isButtonEnabled = false,
                        )
                    )

                    is AddBookActions.Chapter.Delete -> {
                        val updatedChapters = state.value.chapters.toMutableList()
                        updatedChapters.removeAt(index = action.index)
                        updateState(newState = state.value.copy(chapters = updatedChapters))
                    }

                    is AddBookActions.Chapter.Update -> {
                        val updatedChapters = state.value.chapters.toMutableList()
                        updatedChapters[action.index] = action.text
                        updateState(newState = state.value.copy(chapters = updatedChapters))
                        setButtonEnabled()
                    }
                }
            }

            is AddBookActions.NavigateDestination -> updateState(
                newState = state.value.copy(
                    destination = action.destination
                )
            )
        }
    }

    private fun setButtonEnabled() {
        updateState(
            newState = state.value.copy(
                isButtonEnabled = state.value.author.isNotEmpty() && state.value.title.isNotEmpty() &&
                        state.value.chapters.isNotEmpty() && !state.value.chapters.any { it.isBlank() }
            )
        )
    }
}
