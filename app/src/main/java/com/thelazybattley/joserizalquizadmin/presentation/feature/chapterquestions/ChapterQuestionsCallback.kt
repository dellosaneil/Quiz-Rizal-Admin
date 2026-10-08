package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface ChapterQuestionsCallback : BaseCallback<ChapterQuestionsActions> {

    companion object {
        fun default() = object : ChapterQuestionsCallback {
            override fun handleAction(action: ChapterQuestionsActions) = Unit
        }
    }
}
