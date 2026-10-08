package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class ReleaseActions : BaseActions {

    object RequestPush : ReleaseActions()

    object CancelPush : ReleaseActions()

    object ConfirmPush : ReleaseActions()

    object Retry : ReleaseActions()
}
