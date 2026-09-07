package com.thelazybattley.joserizalquizadmin.base

interface BaseCallback<BaseAction> {

    fun handleAction(action: BaseAction)

}
