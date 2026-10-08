package com.thelazybattley.joserizalquizadmin.presentation.feature.login

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInError
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInException
import com.thelazybattley.joserizalquizadmin.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : BaseViewModel<LoginState, LoginActions>(initialState = LoginState()), LoginCallback {

    override fun handleAction(action: LoginActions) {
        when (action) {
            is LoginActions.SignIn -> {
                updateState(newState = state.value.copy(isLoading = true, error = null))
                viewModelScope.launch {
                    signInUseCase(email = action.email.trim(), password = action.password)
                        .onSuccess {
                            updateState(
                                newState = state.value.copy(
                                    isLoading = false,
                                    destination = LoginDestinations.HOME
                                )
                            )
                        }
                        .onFailure { throwable ->
                            val error = (throwable as? SignInException)?.error ?: SignInError.UNKNOWN
                            updateState(newState = state.value.copy(isLoading = false, error = error))
                        }
                }
            }

            is LoginActions.NavigateDestination -> updateState(
                newState = state.value.copy(destination = action.destination)
            )
        }
    }
}
