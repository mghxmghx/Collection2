package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class PersonalInfoViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth
) : ViewModel() {
    companion object {
        const val EMAIL = "email"
        const val USERS = "users"
        const val STATUS = "status"
        const val USER_ID = "userID"
        const val USER_NAME = "userName"
    }

    fun getDetails(id: String): MutableLiveData<PersonalInfoModel> {
        val model = MutableLiveData<PersonalInfoModel>()
        firebaseDatabase.getReference(USERS).child(id).get().addOnSuccessListener {
            model.value = PersonalInfoModel(
                it.child(EMAIL).value.toString(),
                it.child(STATUS).value.toString(),
                it.child(USER_ID).value.toString(),
                it.child(USER_NAME).value.toString()
            )
        }
        return model
    }

    fun getUserID(): MutableLiveData<String> {
        val userID = MutableLiveData<String>()
        firebaseDatabase.getReference(USERS).get().addOnSuccessListener {
            for (i in it.children) {
                if (i.child(EMAIL).value.toString()
                        .equals(auth.currentUser?.email.toString())
                ) {
                    userID.value = i.key.toString()
                }
            }
        }
        return userID
    }

    fun deleteAccount(): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        FirebaseAuth.getInstance().currentUser?.delete()?.addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }

    fun deleteAccountRTDB(id: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = FirebaseDatabase.getInstance().getReference(USERS)
        db.child(id).removeValue().addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }

    fun changePassword(password: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val user = FirebaseAuth.getInstance().currentUser
        user!!.updatePassword(password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isSuccess.value = task.isSuccessful
                println("Update Success")
            } else {
                isSuccess.value = task.isSuccessful
                println("Erorr Update")
            }
        }
        return isSuccess
    }
}