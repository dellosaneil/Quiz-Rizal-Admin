package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class AddBookActions : BaseActions {

    data class AddBook(
        val author: String,
        val bookName: String,
        val category: String
    ) : AddBookActions()

}
