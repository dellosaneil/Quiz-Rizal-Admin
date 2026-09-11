package com.thelazybattley.joserizalquizadmin.data.di.modules

import com.thelazybattley.joserizalquizadmin.data.QuizRepositoryImpl
import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindQuizRepository(quizRepositoryImpl: QuizRepositoryImpl): QuizRepository

}
