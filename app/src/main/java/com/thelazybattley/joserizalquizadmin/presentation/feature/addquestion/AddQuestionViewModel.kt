package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.InsertQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetUpdatedQuizUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddQuestionViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    private val insertQuizUseCase: InsertQuizUseCase,
    savedStateHandle: SavedStateHandle,
    private val setUpdatedQuizUseCase: SetUpdatedQuizUseCase
) : BaseViewModel<AddQuestionState, AddQuestionAction>(
    initialState = AddQuestionState(
        quizId = savedStateHandle.get<String>(QUIZ_ID) ?: throw IllegalStateException("Quiz Id not found"),
        chapterNumber = savedStateHandle.get<Int>(CHAPTER_NUMBER)
            ?: throw IllegalStateException("Chapter Number not found")
    )
), AddQuestionCallback {

    init {
        viewModelScope.launch {
            getQuizByIdUseCase(id = state.value.quizId).collect { quiz ->
                updateState(newState = state.value.copy(quiz = quiz))
            }
        }
    }

    override fun handleAction(action: AddQuestionAction) {
        if (action is AddQuestionAction.Navigate) {
            updateState(newState = state.value.copy(destination = action.destination))
            return
        }
        if (state.value.isSaving) return

        when (action) {
            is AddQuestionAction.Choice.Selected -> updateState(
                newState = state.value.copy(correctAnswerIndex = action.index)
            )

            is AddQuestionAction.Choice.UpdateValue -> updateState(
                newState = state.value.copy(
                    choices = state.value.choices.mapIndexed { index, choice ->
                        if (index == action.index) action.choice else choice
                    }
                ).clearSavedBannerIf(typed = action.choice.isNotEmpty())
            )

            is AddQuestionAction.UpdateQuestion -> updateState(
                newState = state.value.copy(question = action.question)
                    .clearSavedBannerIf(typed = action.question.isNotEmpty())
            )

            AddQuestionAction.SaveQuestion -> save()

            is AddQuestionAction.Navigate -> Unit
        }
    }

    // The saved banner stays until the admin starts on the next question.
    private fun AddQuestionState.clearSavedBannerIf(typed: Boolean) =
        if (typed) copy(lastSaved = null) else this

    private fun save() {
        val current = state.value
        val quiz = current.quiz ?: return
        val chapter = current.chapter ?: return
        if (current.blocker != null) return

        val choices = current.choices.map { it.trim() }
        val newQuestion = Question(
            question = current.question.trim(),
            choices = choices,
            answer = choices[current.correctAnswerIndex]
        )
        val updatedQuiz = quiz.copy(
            chapters = quiz.chapters.map {
                if (it.chapterNumber == current.chapterNumber) it.copy(questions = it.questions + newQuestion) else it
            }
        )
        updateState(newState = current.copy(isSaving = true, saveFailed = false))
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    setUpdatedQuizUseCase(quiz = updatedQuiz)
                    insertQuizUseCase(quiz = listOf(updatedQuiz))
                }
            }
                .onSuccess {
                    updateState(
                        newState = state.value.copy(
                            quiz = updatedQuiz,
                            question = "",
                            choices = List(size = AddQuestionState.CHOICE_COUNT) { "" },
                            correctAnswerIndex = -1,
                            isSaving = false,
                            lastSaved = SavedQuestion(
                                number = chapter.questions.size.inc(),
                                question = newQuestion.question
                            ),
                            formVersion = state.value.formVersion.inc(),
                            savedCount = state.value.savedCount.inc()
                        )
                    )
                }
                .onFailure {
                    updateState(newState = state.value.copy(isSaving = false, saveFailed = true))
                }
        }
    }
}
