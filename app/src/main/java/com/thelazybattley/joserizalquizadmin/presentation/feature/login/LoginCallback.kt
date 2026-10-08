package com.thelazybattley.joserizalquizadmin.presentation.feature.login

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface LoginCallback : BaseCallback<LoginActions> {

    companion object {
        fun default() = object : LoginCallback {
            override fun handleAction(action: LoginActions) {
                TODO("Not yet implemented")
            }
        }
    }
}
