package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AddQuestionState, AddQuestionAction>(initialState = AddQuestionState()),
    AddQuestionCallback {


    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            val quizId =
                savedStateHandle.get<String>(QUIZ_ID) ?: throw Exception("Quiz Id not found")
            val chapterNumber = savedStateHandle.get<Int>(CHAPTER_NUMBER)
                ?: throw Exception("Chapter Number not found")
            getQuizByIdUseCase(id = quizId).also { quiz ->
                updateState(
                    newState = state.value.copy(
                        quiz = quiz, chapterNumber = chapterNumber,
                        quizId = quizId
                    )
                )
            }
        }
    }

    override fun handleAction(action: AddQuestionAction) {
        when (action) {
            is AddQuestionAction.Choice -> {
                when (action) {
                    is AddQuestionAction.Choice.Selected -> updateState(
                        newState = state.value.copy(
                            correctAnswerIndex = action.index
                        )
                    )

                    is AddQuestionAction.Choice.UpdateValue -> updateState(
                        newState = state.value.copy(
                            choices = state.value.choices.toMutableList().apply {
                                this[action.index] = action.choice
                            }
                        )
                    )
                }
            }

            is AddQuestionAction.Navigate -> updateState(
                newState = state.value.copy(
                    destination = action.destination
                )
            )

            is AddQuestionAction.UpdateQuestion -> updateState(
                newState = state.value.copy(
                    question = action.question
                )
            )

            AddQuestionAction.SaveQuestion -> {
                println("Test: ${state.value}")
            }
        }
    }
}
