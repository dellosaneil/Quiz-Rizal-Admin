package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import com.thelazybattley.joserizalquizadmin.base.BaseActions
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange

sealed class ReleaseActions : BaseActions {

    object RequestPush : ReleaseActions()

    object CancelPush : ReleaseActions()

    object ConfirmPush : ReleaseActions()

    object Retry : ReleaseActions()

    data class RequestRevert(val change: ReleaseChange) : ReleaseActions()

    object CancelRevert : ReleaseActions()

    object ConfirmRevert : ReleaseActions()
}
