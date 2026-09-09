package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseActions
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

sealed class AddBookActions : BaseActions {

    data class TextFieldUpdated(
        val text: String,
        val type: AddBookTextFieldTypes
    ) : AddBookActions()

    object PublishBook : AddBookActions()

    data class CategoryUpdated(
        val category: Category
    ) : AddBookActions()

}
