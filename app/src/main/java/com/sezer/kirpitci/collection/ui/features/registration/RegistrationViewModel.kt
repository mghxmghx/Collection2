package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth
) : ViewModel() {
    companion object {
        const val USERS = "users"
        const val USER_ID = "userID"
        const val FALSE = "false"
        const val CARDS = "cards"
    }

    fun getMaxId(): MutableLiveData<Int> {
        val db = firebaseDatabase.getReference(USERS)
        val lastInt = MutableLiveData<Int>()
        db.get().addOnSuccessListener {
            var lastIndex = 0
            for (child in it.children) {
                lastIndex = child.child(USER_ID).value.toString().toInt()
            }
            lastInt.value = lastIndex
        }
        return lastInt
    }

    fun createUser(model: CreateUserModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(model.email, model.password).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }

    fun createStatus(model: AddUserModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        addUserInCard(model.userID, model.userName, model.email, FALSE)
        val db = firebaseDatabase.getReference(USERS)
        db.child(model.userID.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }

    fun addUserInCard(userID: String, userKey: String, userMail: String, status: String) {
        val userMailSplit = userMail.split("@")
        val db = firebaseDatabase.getReference(CARDS)
        val model = RegisterModel(userMailSplit.get(0), status)
        db.get().addOnSuccessListener {
            for (children in it.children) {
                val db2 = firebaseDatabase.getReference(CARDS).child(children.key.toString())
                    .child(USERS)
                db2.child(userID).setValue(model)
            }
        }
    }
}