package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface ModerateCallback : BaseCallback<ModerateActions> {

    companion object {
        fun default () = object: ModerateCallback {
            override fun handleAction(action: ModerateActions) {
                TODO("Not yet implemented")
            }
        }
    }
}
