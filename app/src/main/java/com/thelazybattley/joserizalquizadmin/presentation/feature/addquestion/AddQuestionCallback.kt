package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface AddQuestionCallback: BaseCallback<AddQuestionAction> {

    companion object {
        fun default() = object : AddQuestionCallback {
            override fun handleAction(action: AddQuestionAction) {
                TODO("Not yet implemented")
            }
        }
    }
}
