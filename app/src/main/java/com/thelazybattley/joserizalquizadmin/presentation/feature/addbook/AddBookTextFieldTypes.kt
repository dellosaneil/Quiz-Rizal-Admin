package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

enum class AddBookTextFieldTypes(@StringRes val id: Int) {

    TITLE(id = R.string.title),
    AUTHOR(id = R.string.author)

}
