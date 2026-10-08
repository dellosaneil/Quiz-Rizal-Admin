package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseActions
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

sealed class ContentActions : BaseActions {

    data class Navigate(val destination: ContentDestinations?): ContentActions()

    data class ExpandBook(val id: String?): ContentActions()

    data class FilterSelected(val category: Category?): ContentActions()

}
