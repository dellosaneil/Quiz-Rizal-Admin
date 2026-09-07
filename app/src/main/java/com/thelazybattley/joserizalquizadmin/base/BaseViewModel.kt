package com.thelazybattley.joserizalquizadmin.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<BaseState, BaseAction> : ViewModel(), BaseCallback<BaseAction> {

    private val _state = MutableStateFlow<BaseState?>(value = null)
    val state = _state.asStateFlow()

    abstract override fun handleAction(action: BaseAction)

    fun updateState(updatedState: BaseState) = _state.update {
        updatedState
    }
}
