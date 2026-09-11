package com.thelazybattley.joserizalquizadmin.presentation.util

import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

enum class Category( @StringRes val id: Int) {
    LIFE_OF_RIZAL(id = R.string.life_of_rizal),
    NOVEL(id = R.string.novel);

    companion object {
        fun fromString(value: String): Category {
            return when (value.lowercase()) {
                "novel" -> NOVEL
                "life_of_rizal" -> LIFE_OF_RIZAL
                else -> throw IllegalArgumentException("Invalid content category: $value")
            }
        }
    }
}
