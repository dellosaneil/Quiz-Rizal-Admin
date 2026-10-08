package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

enum class AddBookTextFieldTypes(@StringRes val id: Int, @StringRes val placeholderId: Int) {

    TITLE(id = R.string.title, placeholderId = R.string.title_placeholder),
    AUTHOR(id = R.string.author, placeholderId = R.string.author_placeholder)

}
