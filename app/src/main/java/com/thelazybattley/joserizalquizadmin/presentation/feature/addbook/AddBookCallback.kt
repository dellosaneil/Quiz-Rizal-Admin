package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface AddBookCallback: BaseCallback<AddBookActions> {

    companion object {
        fun default() = object: AddBookCallback {
            override fun handleAction(action: AddBookActions) {
                TODO("Not yet implemented")
            }
        }
    }
}
