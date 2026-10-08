package com.thelazybattley.joserizalquizadmin.domain.model.auth

enum class SignInError {
    INVALID_CREDENTIALS,
    PROVIDER_DISABLED,
    TOO_MANY_ATTEMPTS,
    NETWORK,
    UNKNOWN
}

class SignInException(val error: SignInError, cause: Throwable) : Exception(cause)
