package org.sandec.hanippund.simpleretrofit.preference

import android.content.Context
import androidx.core.content.edit

class PreferenceHelper(context: Context) {

    companion object {
        private const val PREF_FILE_NAME = "SAMPLE_RETROFIT"
        private const val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"
        private const val PREF_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN"
    }

    private val preference = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = preference.getBoolean(PREF_IS_LOGGED_IN, false)
        set(value) = preference.edit { putBoolean(PREF_IS_LOGGED_IN, value) }

    var token: String?
        get() = preference.getString(PREF_KEY_TOKEN, null)
        set(value) = preference.edit { putString(PREF_KEY_TOKEN, value) }

    fun succesLogin(tokenBearer: String){
        preference.edit {
            isLoggedIn = true
            token = tokenBearer
        }
    }

    fun succesLogout(){
        preference.edit {
            isLoggedIn = false
            token = null
        }
    }
}