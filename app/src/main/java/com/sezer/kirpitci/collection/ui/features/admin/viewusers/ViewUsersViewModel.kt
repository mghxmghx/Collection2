package com.sezer.kirpitci.collection.ui.features.admin.viewusers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.calisma.TrainingModelReal
import javax.inject.Inject

class ViewUsersViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase) : ViewModel() {
    companion object {
        const val EMAIL = "email"
        const val STATUS = "status"
        const val USER_NAME = "userName"
        const val USERS = "users"
    }

    fun getUsers(): MutableLiveData<List<TrainingModelReal>> {
        val list = MutableLiveData<List<TrainingModelReal>>()
        val cardList = ArrayList<TrainingModelReal>()
        val db2 = firebaseDatabase.getReference(USERS)
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        TrainingModelReal(
                            child.child(EMAIL).value.toString(),
                            child.child(STATUS).value.toString(),
                            child.child(USER_NAME).value.toString()
                        )
                    )
                }
                list.value = cardList
            }
            .addOnFailureListener {}
        return list
    }
}