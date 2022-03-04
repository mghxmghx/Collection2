package com.sezer.kirpitci.collection.utis.others

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesClass {
    private val fileName = "SharedUserInformation"
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var USER_EMAIL = "Email"
    private var USER_PASSWORD = "Password"
    private var LANGUAGE = "Language"

    fun instantPref(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun addUserEmail(email: String) {
        editor.putString(USER_EMAIL, email)
        editor.apply()
        editor.commit()
    }

    fun addUserPassword(password: String) {
        editor.putString(USER_PASSWORD, password)
        editor.apply()
        editor.commit()
    }

    fun getEmail(): String? {
        var sharedValue = sharedPreferences.getString(USER_EMAIL, "")
        return sharedValue
    }

    fun getPassword(): String? {
        var sharedValue = sharedPreferences.getString(USER_PASSWORD, "")
        return sharedValue
    }

    fun setCompanyLanguage(language: String) {
        editor.putString(LANGUAGE, language)
            .apply()
        editor.commit()
    }

    fun getCompanyLanguage(): String? {
        var sharedValue = sharedPreferences.getString(LANGUAGE, "")
        return sharedValue
    }
}