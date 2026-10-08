package com.thelazybattley.joserizalquizadmin.presentation.feature.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInError
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginState
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTextField
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginDestinations) -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = LoginActions.NavigateDestination(destination = null))
        }
    }

    LoginScreen(
        modifier = modifier,
        state = state,
        callback = viewModel
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    callback: LoginCallback
) {
    val email = rememberTextFieldState()
    val password = rememberTextFieldState()
    val signIn = {
        callback.handleAction(
            action = LoginActions.SignIn(
                email = email.text.toString(),
                password = password.text.toString()
            )
        )
    }
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                style = typography.bold23,
                color = colors.espresso
            )
            Text(
                text = stringResource(id = R.string.sign_in_description),
                style = typography.regular12,
                color = colors.taupe
            )
            Column {
                Text(
                    text = stringResource(id = R.string.email),
                    style = typography.semiBold10,
                    color = colors.taupe
                )
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 65.dp),
                    state = email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    textAlign = TextAlign.Left,
                    textStyle = typography.regular13
                )
            }
            Column {
                Text(
                    text = stringResource(id = R.string.password),
                    style = typography.semiBold10,
                    color = colors.taupe
                )
                LoginPasswordField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 65.dp),
                    state = password,
                    onDone = signIn
                )
            }
            state.error?.let { error ->
                val textRes = when (error) {
                    SignInError.INVALID_CREDENTIALS -> R.string.sign_in_invalid_credentials
                    SignInError.PROVIDER_DISABLED -> R.string.sign_in_provider_disabled
                    SignInError.TOO_MANY_ATTEMPTS -> R.string.sign_in_too_many_attempts
                    SignInError.NETWORK -> R.string.sign_in_network
                    SignInError.UNKNOWN -> R.string.sign_in_failed
                }
                Text(
                    text = stringResource(id = textRes),
                    style = typography.regular12,
                    color = colors.brickRed
                )
            }
            CommonButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_in),
                enabled = !state.isLoading
            ) {
                signIn()
            }
        }
    }
}

// Mirrors CommonTextField's styling, but masks the input.
@Composable
private fun LoginPasswordField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onDone: () -> Unit
) {
    BasicSecureTextField(
        modifier = modifier
            .padding(vertical = 12.dp)
            .border(
                width = 1.dp,
                color = colors.espresso,
                shape = RoundedCornerShape(size = 12.dp)
            )
            .background(color = colors.ivoryMist, shape = RoundedCornerShape(size = 12.dp))
            .clip(shape = RoundedCornerShape(size = 12.dp)),
        state = state,
        textStyle = typography.regular13.copy(color = colors.espresso),
        cursorBrush = SolidColor(value = colors.maroon),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        onKeyboardAction = { onDone() },
        decorator = TextFieldDecorator { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        LoginScreen(
            modifier = Modifier,
            state = LoginState(error = SignInError.INVALID_CREDENTIALS),
            callback = LoginCallback.default()
        )
    }
}
