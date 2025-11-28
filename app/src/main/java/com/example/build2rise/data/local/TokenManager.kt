package com.example.build2rise.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")



class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
        private val LIKED_POSTS_STRING = stringPreferencesKey("liked_posts_string")

    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveUserType(userType: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TYPE_KEY] = userType
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    fun getUserType(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_TYPE_KEY]
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    // Save liked posts as comma-separated string
    suspend fun saveLikedPosts(postIds: Set<String>) {
        val combined = postIds.joinToString(",")  // "id1,id2,id3"
        context.dataStore.edit { prefs ->
            prefs[LIKED_POSTS_STRING] = combined
        }
    }

    // Load liked posts and convert back to Set<String>
    fun getLikedPosts(): Flow<Set<String>> {
        return context.dataStore.data.map { prefs ->
            prefs[LIKED_POSTS_STRING]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }
    }

    // Clear liked posts only
    suspend fun clearLikedPosts() {
        context.dataStore.edit { prefs ->
            prefs.remove(LIKED_POSTS_STRING)
        }
    }

}
