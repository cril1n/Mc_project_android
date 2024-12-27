package com.example.mangiaebasta.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mangiaebasta.model.GetUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatastoreManager(private val dataStore: DataStore<Preferences>)  {
    private val SID_KEY = stringPreferencesKey("sid_key")

    fun setSidInDataStore(sid: String?) {
        if (sid == null) return
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit { preferences ->
                preferences[SID_KEY] = sid
                val savedSid = preferences[SID_KEY]
                Log.d("StorageManager", "New saved sid: $savedSid")
            }
        }
    }

    suspend fun getSidFromDataStore(): String? {
        Log.d("StorageManager", "Getting sid from data store")
        return withContext(Dispatchers.IO) {
            val preferences = dataStore.data.first()
            preferences[SID_KEY]
        }
    }

    private val UID_KEY = intPreferencesKey("uid_key")

    fun setUidInDataStore(uid: Int?) {
        if (uid == null) return
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.edit { preferences ->
                preferences[UID_KEY] = uid
                val savedUid = preferences[UID_KEY]
                Log.d("StorageManager", "New saved uid: $savedUid")
            }
        }
    }

    suspend fun getUidFromDataStore(): Int? {
        Log.d("StorageManager", "Getting sid from data store")
        return withContext(Dispatchers.IO) {
            val preferences = dataStore.data.first()
            preferences[UID_KEY]
        }
    }

    val LASTSCREEN_KEY = stringPreferencesKey("LastScreen_key")

    fun setLastScreenInDataStore(lastScreen: String?) {
        if (lastScreen == null) return
        CoroutineScope(Dispatchers.Main).launch {
            dataStore?.edit { preferences ->
                preferences[LASTSCREEN_KEY] = lastScreen
                val savedLastScreen = preferences[LASTSCREEN_KEY]
                Log.d("DataStoreManager", "New saved screen: $savedLastScreen")
            }
        }
    }

    suspend fun getLastScreenFromDataStore(): String? {
        return withContext(Dispatchers.IO) {
            val preferences = dataStore?.data?.first()
            preferences?.get(LASTSCREEN_KEY)
        }
    }

    fun clearDataStore() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                preferences.clear() // Rimuove tutte le coppie chiave-valore
            }
            Log.d("StorageManager", "Data store cleared")
        }
    }

    //USER
    object PreferencesKeys {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val CARD_FULL_NAME = stringPreferencesKey("card_full_name")
        val CARD_NUMBER = stringPreferencesKey("card_number")
        val CARD_EXPIRE_MONTH = intPreferencesKey("card_expire_month")
        val CARD_EXPIRE_YEAR = intPreferencesKey("card_expire_year")
        val CARD_CVV = stringPreferencesKey("card_cvv")
        val LAST_OID = intPreferencesKey("last_oid")
        val ORDER_STATUS = stringPreferencesKey("order_status")
        val UID = intPreferencesKey("uid")
    }

    suspend fun saveUser( user: GetUserResponse) {
        Log.d("StorageManager", "Saving user: $user")
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_NAME] = user.firstName!!
            preferences[PreferencesKeys.LAST_NAME] = user.lastName!!
            preferences[PreferencesKeys.CARD_FULL_NAME] = user.cardFullName!!
            preferences[PreferencesKeys.CARD_NUMBER] = user.cardNumber!!
            preferences[PreferencesKeys.CARD_EXPIRE_MONTH] = user.cardExpireMonth!!
            preferences[PreferencesKeys.CARD_EXPIRE_YEAR] = user.cardExpireYear!!
            preferences[PreferencesKeys.CARD_CVV] = user.cardCVV!!
            preferences[PreferencesKeys.LAST_OID] = user.lastOid!!
            preferences[PreferencesKeys.ORDER_STATUS] = user.orderStatus!!
            preferences[PreferencesKeys.UID] = user.uid!!

        }
    }

    fun getUser(): Flow<GetUserResponse> {
        return dataStore.data.map { preferences ->
            GetUserResponse(
                firstName = preferences[PreferencesKeys.FIRST_NAME] ?: "",
                lastName = preferences[PreferencesKeys.LAST_NAME] ?: "",
                cardFullName = preferences[PreferencesKeys.CARD_FULL_NAME] ?: "",
                cardNumber = preferences[PreferencesKeys.CARD_NUMBER] ?: "",
                cardExpireMonth = preferences[PreferencesKeys.CARD_EXPIRE_MONTH]?.toInt() ?: 0,
                cardExpireYear = preferences[PreferencesKeys.CARD_EXPIRE_YEAR]?.toInt() ?: 0,
                cardCVV = preferences[PreferencesKeys.CARD_CVV] ?: "",
                lastOid = preferences[PreferencesKeys.LAST_OID]?.toInt() ?: 0,
                orderStatus = preferences[PreferencesKeys.ORDER_STATUS] ?: "",
                uid = preferences[PreferencesKeys.UID]?.toInt() ?: 0,
            )
        }
    }

}