package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.calisma.RegisterModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth): ViewModel() {
    fun createUser(model: CreateUserModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(model.email, model.password).addOnCompleteListener {
            if (it.isSuccessful) {
                isSuccess.value = it.isSuccessful

            } else {
                isSuccess.value = false
            }
        }
        return isSuccess
    }

    fun createStatus(model: AddUserModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        addUserInCard(model.userName, model.email, "false")
        val db = firebaseDatabase.getReference("users")
        db.child(model.userName.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }
    fun addUserInCard(userKey: String, userMail: String, status: String){
        val userMailSplit  = userMail.split("@")
        val db = firebaseDatabase.getReference("cards")
        val model = RegisterModel(userMailSplit.get(0), status)
        db.get().addOnSuccessListener {
            for (children in it.children){
                val db2 = firebaseDatabase.getReference("cards").child(children.key.toString()).child("users")
                db2.child(userKey).setValue(model)
            }
        }
    }
}