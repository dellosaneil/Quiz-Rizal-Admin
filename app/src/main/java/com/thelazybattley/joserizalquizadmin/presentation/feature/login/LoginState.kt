package com.thelazybattley.joserizalquizadmin.presentation.feature.login

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInError

data class LoginState(
    val isLoading: Boolean = false,
    val error: SignInError? = null,
    val destination: LoginDestinations? = null
) : BaseState
