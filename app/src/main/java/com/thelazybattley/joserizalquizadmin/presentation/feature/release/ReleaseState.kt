package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseOverview

data class ReleaseState(
    val overview: ReleaseOverviewState = ReleaseOverviewState.Loading,
    val pushPhase: PushPhase = PushPhase.IDLE,
    // Number of changes the last successful push made live, shown in the success banner.
    val pushedChangeCount: Int? = null,
    val pushFailed: Boolean = false
) : BaseState

sealed interface ReleaseOverviewState {
    data object Loading : ReleaseOverviewState
    data object Failed : ReleaseOverviewState
    data class Loaded(val overview: ReleaseOverview) : ReleaseOverviewState
}

enum class PushPhase {
    IDLE,
    CONFIRMING,
    PUSHING
}
