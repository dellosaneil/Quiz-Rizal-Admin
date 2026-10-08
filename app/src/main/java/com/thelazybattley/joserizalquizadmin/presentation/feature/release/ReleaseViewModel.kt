package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReleaseOverviewUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetQuizContentToReleaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val fetchReleaseOverviewUseCase: FetchReleaseOverviewUseCase,
    private val setQuizContentToReleaseUseCase: SetQuizContentToReleaseUseCase
) : BaseViewModel<ReleaseState, ReleaseActions>(initialState = ReleaseState()), ReleaseCallback {

    init {
        fetchOverview()
    }

    override fun handleAction(action: ReleaseActions) {
        when (action) {
            ReleaseActions.RequestPush -> if (state.value.pushPhase == PushPhase.IDLE) {
                updateState(newState = state.value.copy(pushPhase = PushPhase.CONFIRMING, pushFailed = false))
            }

            ReleaseActions.CancelPush -> if (state.value.pushPhase == PushPhase.CONFIRMING) {
                updateState(newState = state.value.copy(pushPhase = PushPhase.IDLE))
            }

            ReleaseActions.ConfirmPush -> push()

            ReleaseActions.Retry -> fetchOverview()
        }
    }

    private fun fetchOverview() {
        updateState(newState = state.value.copy(overview = ReleaseOverviewState.Loading))
        viewModelScope.launch {
            val overview = runCatching { withContext(context = Dispatchers.IO) { fetchReleaseOverviewUseCase() } }
                .fold(
                    onSuccess = { ReleaseOverviewState.Loaded(overview = it) },
                    onFailure = { ReleaseOverviewState.Failed }
                )
            updateState(newState = state.value.copy(overview = overview))
        }
    }

    private fun push() {
        val overview = (state.value.overview as? ReleaseOverviewState.Loaded)?.overview ?: return
        if (state.value.pushPhase != PushPhase.CONFIRMING) return
        updateState(newState = state.value.copy(pushPhase = PushPhase.PUSHING, pushedChangeCount = null))
        viewModelScope.launch {
            runCatching { withContext(context = Dispatchers.IO) { setQuizContentToReleaseUseCase() } }
                .onSuccess {
                    updateState(
                        newState = state.value.copy(
                            pushPhase = PushPhase.IDLE,
                            pushedChangeCount = overview.changes.size
                        )
                    )
                    // Reload so the counts and change list reflect what's now live.
                    fetchOverview()
                }
                .onFailure {
                    updateState(newState = state.value.copy(pushPhase = PushPhase.IDLE, pushFailed = true))
                }
        }
    }
}
