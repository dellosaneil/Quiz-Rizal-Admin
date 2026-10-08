package com.thelazybattley.joserizalquizadmin.presentation.feature.release

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface ReleaseCallback : BaseCallback<ReleaseActions> {

    companion object {
        fun default() = object : ReleaseCallback {
            override fun handleAction(action: ReleaseActions) {
                TODO("Not yet implemented")
            }
        }
    }
}
