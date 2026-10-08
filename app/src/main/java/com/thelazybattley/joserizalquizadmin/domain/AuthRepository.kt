package com.thelazybattley.joserizalquizadmin.domain

interface AuthRepository {

    fun isSignedIn(): Boolean

    suspend fun signIn(email: String, password: String): Result<Unit>
}
