package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.calisma.RegisterModel
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth): ViewModel() {

    fun getMaxId(): MutableLiveData<Int> {
        val db = firebaseDatabase.getReference("users")
        val lastInt = MutableLiveData<Int>()

        db.get().addOnSuccessListener {
            var lastIndex = 0

            for (child in it.children) {
                lastIndex = child.child("userID").value.toString().toInt()
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
        addUserInCard(model.userID,model.userName, model.email, "false")
        val db = firebaseDatabase.getReference("users")
        db.child(model.userID.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }
    fun addUserInCard(userID:String, userKey: String, userMail: String, status: String){
        val userMailSplit  = userMail.split("@")
        val db = firebaseDatabase.getReference("cards")
        val model = RegisterModel(userMailSplit.get(0), status)
        db.get().addOnSuccessListener {
            for (children in it.children){
                val db2 = firebaseDatabase.getReference("cards").child(children.key.toString()).child("users")
                db2.child(userID).setValue(model)
            }
        }
    }
}