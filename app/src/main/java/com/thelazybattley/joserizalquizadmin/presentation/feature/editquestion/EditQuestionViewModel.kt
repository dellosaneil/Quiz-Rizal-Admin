package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.usecase.DismissReportedQuestionUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetQuizByIdUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.InsertQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetUpdatedQuizUseCase
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUESTION
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditQuestionViewModel @Inject constructor(
    private val getQuizByIdUseCase: GetQuizByIdUseCase,
    private val fetchReportedQuestionsUseCase: FetchReportedQuestionsUseCase,
    private val dismissReportedQuestionUseCase: DismissReportedQuestionUseCase,
    private val setUpdatedQuizUseCase: SetUpdatedQuizUseCase,
    private val insertQuizUseCase: InsertQuizUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<EditQuestionState, EditQuestionActions>(
    initialState = EditQuestionState(
        quizId = savedStateHandle.get<String>(QUIZ_ID) ?: throw IllegalStateException("Quiz Id not found"),
        chapterNumber = savedStateHandle.get<Int>(CHAPTER_NUMBER)
            ?: throw IllegalStateException("Chapter Number not found"),
        originalText = savedStateHandle.get<String>(QUESTION).orEmpty()
    )
), EditQuestionCallback {

    init {
        viewModelScope.launch {
            // Only the first copy: later emissions would be our own save, under the new text.
            val quiz = runCatching { getQuizByIdUseCase(id = state.value.quizId).first() }.getOrNull()
            val questions = quiz?.chapters?.firstOrNull { it.chapterNumber == state.value.chapterNumber }?.questions
            val index = questions?.indexOfFirst { it.question == state.value.originalText } ?: -1
            val original = questions?.getOrNull(index)
            updateState(
                newState = if (original == null) {
                    state.value.copy(quiz = quiz, isNotFound = true)
                } else {
                    state.value.copy(quiz = quiz, questionIndex = index, original = original).withOriginalValues()
                }
            )
        }
        viewModelScope.launch {
            runCatching { withContext(context = Dispatchers.IO) { fetchReportedQuestionsUseCase() } }
                .onSuccess { reports ->
                    val report = reports.firstOrNull {
                        it.quizId == state.value.quizId &&
                                it.chapterNumber == state.value.chapterNumber &&
                                it.question == state.value.originalText
                    }
                    updateState(newState = state.value.copy(report = report))
                }
        }
    }

    private fun EditQuestionState.withOriginalValues(): EditQuestionState {
        val original = original ?: return this
        return copy(
            question = original.question,
            choices = original.choices,
            correctAnswerIndex = original.choices.indexOf(original.answer)
        )
    }

    override fun handleAction(action: EditQuestionActions) {
        if (action is EditQuestionActions.Navigate) {
            updateState(newState = state.value.copy(destination = action.destination))
            return
        }
        if (state.value.isBusy) return

        when (action) {
            is EditQuestionActions.UpdateQuestion -> updateState(newState = state.value.copy(question = action.question))

            is EditQuestionActions.UpdateChoice -> updateState(
                newState = state.value.copy(
                    choices = state.value.choices.mapIndexed { index, choice ->
                        if (index == action.index) action.choice else choice
                    }
                )
            )

            is EditQuestionActions.SelectAnswer -> updateState(
                newState = state.value.copy(correctAnswerIndex = action.index)
            )

            EditQuestionActions.Revert -> updateState(
                newState = state.value.withOriginalValues().copy(formVersion = state.value.formVersion.inc())
            )

            EditQuestionActions.ToggleClearReports -> updateState(
                newState = state.value.copy(clearReports = !state.value.clearReports)
            )

            EditQuestionActions.Save -> save()

            EditQuestionActions.RequestDelete -> updateState(
                newState = state.value.copy(showDeleteSheet = true, deleteFailed = false)
            )

            EditQuestionActions.CancelDelete -> updateState(newState = state.value.copy(showDeleteSheet = false))

            EditQuestionActions.ConfirmDelete -> delete()

            is EditQuestionActions.Navigate -> Unit
        }
    }

    private fun save() {
        val current = state.value
        if (current.blocker != null) return
        val choices = current.choices.map { it.trim() }
        val updated = Question(
            question = current.question.trim(),
            choices = choices,
            answer = choices[current.correctAnswerIndex]
        )
        updateState(newState = current.copy(isSaving = true, saveFailed = false))
        writeChapter(
            transform = { questions -> questions.mapIndexed { index, question -> if (index == current.questionIndex) updated else question } },
            clearReports = current.clearReports,
            onFailure = { updateState(newState = state.value.copy(isSaving = false, saveFailed = true)) }
        )
    }

    private fun delete() {
        val current = state.value
        updateState(newState = current.copy(isDeleting = true, deleteFailed = false))
        writeChapter(
            transform = { questions -> questions.filterIndexed { index, _ -> index != current.questionIndex } },
            // A deleted question's reports can't point anywhere, so they always go.
            clearReports = true,
            onFailure = { updateState(newState = state.value.copy(isDeleting = false, deleteFailed = true)) }
        )
    }

    // Writes the chapter's new question list to Firestore, then the local copy, then leaves.
    private fun writeChapter(
        transform: (List<Question>) -> List<Question>,
        clearReports: Boolean,
        onFailure: () -> Unit
    ) {
        val current = state.value
        val quiz = current.quiz ?: return
        val updatedQuiz: Quiz = quiz.copy(
            chapters = quiz.chapters.map {
                if (it.chapterNumber == current.chapterNumber) it.copy(questions = transform(it.questions)) else it
            }
        )
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    setUpdatedQuizUseCase(quiz = updatedQuiz)
                    insertQuizUseCase(quiz = listOf(updatedQuiz))
                }
            }
                .onSuccess {
                    val report = current.report
                    if (clearReports && report != null) {
                        // The question is already saved; reports that fail to clear stay on the Moderate tab.
                        runCatching { withContext(context = Dispatchers.IO) { dismissReportedQuestionUseCase(reportedQuestion = report) } }
                    }
                    updateState(newState = state.value.copy(destination = EditQuestionDestinations.Back))
                }
                .onFailure { onFailure() }
        }
    }
}
