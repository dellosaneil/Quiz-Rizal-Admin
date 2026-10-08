package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface EditQuestionCallback : BaseCallback<EditQuestionActions> {

    companion object {
        fun default() = object : EditQuestionCallback {
            override fun handleAction(action: EditQuestionActions) = Unit
        }
    }
}
