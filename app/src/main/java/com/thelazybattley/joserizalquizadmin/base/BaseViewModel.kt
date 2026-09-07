package com.thelazybattley.joserizalquizadmin.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<BaseState, BaseAction> : ViewModel(), BaseCallback<BaseAction> {

    private val _state = MutableStateFlow<BaseState?>(value = null)
    val state = _state.value

    abstract override fun handleAction(action: BaseAction)

    fun updateState(state: BaseState) = _state.update {
        state
    }
}
