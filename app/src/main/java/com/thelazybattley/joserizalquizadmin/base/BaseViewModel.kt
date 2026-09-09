package com.thelazybattley.joserizalquizadmin.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<BaseState, BaseAction> : ViewModel(), BaseCallback<BaseAction> {

    private lateinit var _state: MutableStateFlow<BaseState>
    val state = _state.asStateFlow()

    abstract override fun handleAction(action: BaseAction)

    fun initState(initState: MutableStateFlow<BaseState>) {
        _state = initState
    }

    fun updateState(newState: BaseState) = _state.update {
        newState
    }
}
