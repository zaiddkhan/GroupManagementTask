package com.webview.groupmanagementtask.core.di

import com.webview.groupmanagementtask.feature.data.repository.UserDataStoreImpl
import com.webview.groupmanagementtask.feature.domain.repository.UserDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RegisterModule {

    @Binds
    @Singleton
    abstract fun bindUserDataInterfaceToImplementation(
        userDataStoreImpl: UserDataStoreImpl
    ) : UserDataStore



}