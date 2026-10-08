package com.thelazybattley.joserizalquizadmin.presentation.feature.login

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class LoginActions : BaseActions {

    data class SignIn(val email: String, val password: String) : LoginActions()

    data class NavigateDestination(val destination: LoginDestinations?) : LoginActions()
}
