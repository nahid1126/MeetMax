package com.nahid.meetmax.di

import com.nahid.meetmax.di.qualifier.SignInQualifier
import com.nahid.meetmax.di.qualifier.SignUpQualifier
import com.nahid.meetmax.di.qualifier.UserQualifier
import com.nahid.meetmax.model.repository.SignInRepository
import com.nahid.meetmax.model.repository.SignInRepositoryImpl
import com.nahid.meetmax.model.repository.SignUpRepository
import com.nahid.meetmax.model.repository.SignUpRepositoryImpl
import com.nahid.meetmax.model.repository.UserRepository
import com.nahid.meetmax.model.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryProvider {
    @Binds
    @SignUpQualifier
    abstract fun bindSignUpRepository(signUpRepositoryImpl: SignUpRepositoryImpl): SignUpRepository

    @Binds
    @SignInQualifier
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl): SignInRepository

    @Binds
    @UserQualifier
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}