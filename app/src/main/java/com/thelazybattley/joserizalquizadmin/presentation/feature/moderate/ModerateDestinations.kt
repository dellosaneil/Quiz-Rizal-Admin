package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

sealed class ModerateDestinations {
    data class AddBook(val bookTitle: String, val author: String) : ModerateDestinations()
}
