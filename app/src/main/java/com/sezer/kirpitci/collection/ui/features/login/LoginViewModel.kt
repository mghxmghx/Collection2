package com.sezer.kirpitci.collection.ui.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth
) : ViewModel() {
    companion object {
        const val USERS = "users"
        const val EMAIL = "email"
        const val STATUS = "status"
    }

    fun auth(id: String, password: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        if (!id.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(id, password).addOnCompleteListener {
                isSuccess.value = it.isSuccessful
            }
        } else {
            isSuccess.value = false
        }
        return isSuccess
    }

    fun getStatus(): MutableLiveData<String> {
        val personStatues = MutableLiveData<String>()
        val db2 = firebaseDatabase.getReference(USERS)
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    if (child.child(EMAIL).value.toString()
                            .equals(auth.currentUser?.email)
                    ) {
                        personStatues.value = child.child(STATUS).value.toString()
                    }
                }
            }
        return personStatues
    }
}