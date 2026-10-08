package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.AuthRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke() = repository.isSignedIn()

}
