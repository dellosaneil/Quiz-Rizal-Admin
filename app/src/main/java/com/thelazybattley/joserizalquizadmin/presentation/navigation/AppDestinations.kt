package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

sealed class AppDestinations {

    sealed class BottomNavDestinations(
        val route: String, @DrawableRes val drawable: Int,
        @StringRes val textRes: Int
    ) : AppDestinations() {
        object Home : BottomNavDestinations(
            route = "home",
            drawable = R.drawable.ic_home,
            textRes = R.string.home
        )

        object Moderate : BottomNavDestinations(
            route = "moderate",
            drawable = R.drawable.ic_flag,
            textRes = R.string.moderate
        )

        object Content : BottomNavDestinations(
            route = "content",
            drawable = R.drawable.ic_content,
            textRes = R.string.content
        )

        object More : BottomNavDestinations(
            route = "more",
            drawable = R.drawable.ic_more,
            textRes = R.string.more
        )

        companion object {
            fun routes() = listOf(
                Home, Moderate, Content, More
            )
        }
    }
}
