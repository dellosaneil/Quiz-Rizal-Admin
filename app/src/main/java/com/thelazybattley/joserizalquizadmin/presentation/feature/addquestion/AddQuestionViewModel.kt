package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetUpdatedQuizUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    savedStateHandle: SavedStateHandle,
    private val setUpdatedQuizUseCase: SetUpdatedQuizUseCase
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
                setButtonEnabled()
            }

            is AddQuestionAction.Navigate -> updateState(
                newState = state.value.copy(
                    destination = action.destination
                )
            )

            is AddQuestionAction.UpdateQuestion -> {
                updateState(
                    newState = state.value.copy(
                        question = action.question
                    )
                )
                setButtonEnabled()
            }

            AddQuestionAction.SaveQuestion -> {
                val currentState = state.value
                val quiz = currentState.quiz ?: return

                val newQuestion = Question(
                    question = currentState.question,
                    choices = currentState.choices,
                    answer = currentState.choices.getOrNull(currentState.correctAnswerIndex)
                        .orEmpty()
                )

                val updatedQuiz = quiz.copy(
                    chapters = quiz.chapters.map { chapter ->
                        if (chapter.chapterNumber == currentState.chapterNumber) {
                            chapter.copy(questions = chapter.questions + newQuestion)
                        } else {
                            chapter
                        }
                    }
                )
                setUpdatedQuizUseCase(quiz = updatedQuiz)
                updateState(
                    newState = state.value.copy(
                        showSuccessBanner = true,
                        choices = listOf("", "", "", ""),
                        correctAnswerIndex = -1,
                        question = "",
                        isButtonEnabled = false,


                    )
                )

            }

            AddQuestionAction.ResetBanner -> updateState(
                newState = state.value.copy(
                    showSuccessBanner = false
                )
            )
        }
    }

    private fun setButtonEnabled() {
        val buttonEnabled = with(receiver = state.value) {
            question.isNotBlank() && choices.all { it.isNotBlank() } && correctAnswerIndex != -1
        }
        updateState(
            newState = state.value.copy(
                isButtonEnabled = buttonEnabled
            )
        )
    }
}
