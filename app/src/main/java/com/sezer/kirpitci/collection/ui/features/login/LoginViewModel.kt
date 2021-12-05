package com.sezer.kirpitci.collection.ui.features.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class LoginViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth): ViewModel() {
    fun auth(id: String, password: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        if (!id.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(id, password).addOnCompleteListener {


                if (it.isSuccessful) {
                    isSuccess.value = it.isSuccessful
                } else {
                    isSuccess.value = it.isSuccessful
                }
            }
        } else {
            isSuccess.value = false
        }
        return isSuccess
    }

    fun getStatus(): MutableLiveData<String> {
        val personStatues = MutableLiveData<String>()
        val db2 = firebaseDatabase.getReference("users")
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    if (child.child("email").value.toString()
                            .equals(auth.currentUser?.email)
                    ) {
                        personStatues.value = child.child("status").value.toString()
                    }
                }
            }
        return personStatues
    }

}