package com.sezer.kirpitci.collection.ui.features.admin.viewusers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.ui.features.registration.calisma.TrainingModelReal

class ViewUsersViewModel:ViewModel() {

    fun getUsers (): MutableLiveData<List<TrainingModelReal>> {
        val list = MutableLiveData<List<TrainingModelReal>>()
        val cardList = ArrayList<TrainingModelReal>()

        val db = FirebaseDatabase.getInstance()
        val db2 = db.getReference("users")
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        TrainingModelReal(
                            child.child("email").getValue().toString(),
                            child.child("status").getValue().toString(),
                            child.child("userName").getValue().toString()
                        )
                    )
            }
                list.value=cardList
    }
            .addOnFailureListener{}
        return list
}
}