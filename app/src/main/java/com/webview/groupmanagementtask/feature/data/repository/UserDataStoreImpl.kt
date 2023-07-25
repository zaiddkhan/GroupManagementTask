package com.webview.groupmanagementtask.feature.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.webview.groupmanagementtask.feature.data.entities.UserDto
import com.webview.groupmanagementtask.core.data.room.Database
import com.webview.groupmanagementtask.feature.domain.repository.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class UserDataStoreImpl @Inject constructor(
    private val dataStore : DataStore<Preferences>,
    private val db : Database
) : UserDataStore{

    override suspend fun setName(name: String) {
        dataStore.edit {  preferences ->
            preferences[KEY_NAME] = name
        }
    }

    override fun getName(): Flow<String> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preference ->
                preference[KEY_NAME] ?: ""
            }
    }




    override suspend fun setUserRegistered(saved: Boolean) {
        dataStore.edit {  preferences ->
            preferences[KEY_USER_REGISTERED] = saved
        }
    }


    override suspend fun insertUser(userDto: UserDto) {
        db.userDao.insertUser(userDto)
    }

     companion object{

        val KEY_NAME = stringPreferencesKey(
            name = "username"
        )

        val KEY_USER_REGISTERED = booleanPreferencesKey(
            name = "user_registered"
        )
    }
}
