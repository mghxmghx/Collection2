package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class PersonalSettingsViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth
) : ViewModel() {
    companion object {
        const val ALCOHOL_REQUEST = "alcoholRequest"
    }
    fun sendRequest(model: SendRequestModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        firebaseDatabase.getReference(ALCOHOL_REQUEST).child(model.cardName).setValue(model)
            .addOnCompleteListener {
                isSuccess.value = it.isSuccessful
            }
        return isSuccess
    }
}