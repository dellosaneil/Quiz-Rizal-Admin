package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReleaseOverviewUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RevertReleaseChangeUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.SetQuizContentToReleaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val fetchReleaseOverviewUseCase: FetchReleaseOverviewUseCase,
    private val setQuizContentToReleaseUseCase: SetQuizContentToReleaseUseCase,
    private val revertReleaseChangeUseCase: RevertReleaseChangeUseCase
) : BaseViewModel<ReleaseState, ReleaseActions>(initialState = ReleaseState()), ReleaseCallback {

    init {
        fetchOverview()
    }

    override fun handleAction(action: ReleaseActions) {
        when (action) {
            ReleaseActions.RequestPush -> if (state.value.pushPhase == PushPhase.IDLE && !state.value.isReverting) {
                updateState(
                    newState = state.value.copy(pushPhase = PushPhase.CONFIRMING, pushFailed = false, revertSucceeded = false)
                )
            }

            ReleaseActions.CancelPush -> if (state.value.pushPhase == PushPhase.CONFIRMING) {
                updateState(newState = state.value.copy(pushPhase = PushPhase.IDLE))
            }

            ReleaseActions.ConfirmPush -> push()

            ReleaseActions.Retry -> fetchOverview()

            is ReleaseActions.RequestRevert -> if (state.value.pushPhase == PushPhase.IDLE && !state.value.isReverting) {
                updateState(newState = state.value.copy(pendingRevert = action.change, revertFailed = false))
            }

            ReleaseActions.CancelRevert -> if (!state.value.isReverting) {
                updateState(newState = state.value.copy(pendingRevert = null, revertFailed = false))
            }

            ReleaseActions.ConfirmRevert -> revert()
        }
    }

    private fun revert() {
        val change = state.value.pendingRevert ?: return
        if (state.value.isReverting) return
        updateState(
            newState = state.value.copy(
                isReverting = true,
                revertFailed = false,
                revertSucceeded = false,
                pushedChangeCount = null,
                pushFailed = false
            )
        )
        viewModelScope.launch {
            runCatching { withContext(context = Dispatchers.IO) { revertReleaseChangeUseCase(change = change) } }
                .onSuccess {
                    updateState(
                        newState = state.value.copy(pendingRevert = null, isReverting = false, revertSucceeded = true)
                    )
                    // Reload so the reverted change drops off the list.
                    fetchOverview()
                }
                .onFailure {
                    updateState(newState = state.value.copy(isReverting = false, revertFailed = true))
                }
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
