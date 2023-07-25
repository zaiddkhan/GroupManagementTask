package com.webview.groupmanagementtask.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import com.webview.groupmanagementtask.core.data.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import com.webview.groupmanagementtask.feature.data.repository.ChatScreenRepositoryImpl
import com.webview.groupmanagementtask.feature.data.repository.GroupDetailRepositoryImpl
import com.webview.groupmanagementtask.feature.data.repository.GroupListRepositoryImpl
import com.webview.groupmanagementtask.feature.domain.repository.ChatScreenRepository
import com.webview.groupmanagementtask.feature.domain.repository.GroupDetailRepository
import com.webview.groupmanagementtask.feature.domain.repository.GroupListRepository
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateGroupDetailsUseCase
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateMessageUseCase
import com.webview.groupmanagementtask.feature.domain.use_case.ValidateRegisterUserUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): Database {
           return  Room.databaseBuilder(
               applicationContext,
               Database::class.java,
            "app_data.db")
               .fallbackToDestructiveMigration()
               .build()
     }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences>{
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile("user_data") }
        )
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase() = ValidateRegisterUserUseCase()


    @Provides
    @Singleton
    fun provideGroupValidateUseCase() = ValidateGroupDetailsUseCase()

    @Provides
    @Singleton
    fun provideMessageValidateUseCase() = ValidateMessageUseCase()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context : Context) : WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideRepository(db : Database) : GroupListRepository{
        return  GroupListRepositoryImpl(db)

    }

    @Provides
    fun providesChatRepository(db : Database) : ChatScreenRepository{
        return  ChatScreenRepositoryImpl(db)
    }

    @Provides
    fun provideGroupDetailRepository(db : Database) : GroupDetailRepository {
        return GroupDetailRepositoryImpl(db)
    }

}


