package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseCallback

interface ContentCallback : BaseCallback<ContentActions>{

    companion object {
        fun default() = object: ContentCallback {
            override fun handleAction(action: ContentActions) {
                TODO("Not yet implemented")
            }
        }
    }
}
