package com.thelazybattley.joserizalquizadmin.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.thelazybattley.joserizalquizadmin.domain.AuthRepository
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInError
import com.thelazybattley.joserizalquizadmin.domain.model.auth.SignInException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun isSignedIn() = auth.currentUser != null

    override suspend fun signIn(email: String, password: String) = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }.recoverCatching { throwable ->
        throw SignInException(error = throwable.toSignInError(), cause = throwable)
    }

    private fun Throwable.toSignInError() = when (this) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> SignInError.INVALID_CREDENTIALS
        is FirebaseTooManyRequestsException -> SignInError.TOO_MANY_ATTEMPTS
        is FirebaseNetworkException -> SignInError.NETWORK
        // Email/Password is not enabled under Authentication > Sign-in method.
        is FirebaseAuthException if errorCode == "ERROR_OPERATION_NOT_ALLOWED" -> SignInError.PROVIDER_DISABLED
        else -> SignInError.UNKNOWN
    }
}
