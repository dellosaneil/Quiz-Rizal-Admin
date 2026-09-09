package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

data class AddBookState(
    val isLoading: Boolean = true,
    val category: Category = Category.LIFE_OF_RIZAL,
    val author: String = "",
    val bookName: String = "",
    val chapters: List<String> = emptyList(),
    ) : BaseState
