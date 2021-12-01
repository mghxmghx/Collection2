package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
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
        getCardNames()
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.child(model.userName.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }

    fun getCardNames(): MutableLiveData<List<CardModel>> {
        val list = MutableLiveData<List<CardModel>>()
        val cardList = ArrayList<CardModel>()
        val db = FirebaseDatabase.getInstance()
        val db2 = db.getReference("cards")
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        CardModel(
                            child.child("cardID").value.toString(),
                            child.child("cardName").value.toString(),
                            child.child("cardInfo").value.toString(),
                            child.child("cardCategory").value.toString(),
                            child.child("cardCounty").value.toString(),
                            child.child("cardCity").value.toString(),
                            child.child("cardPrice").value.toString(),
                            child.child("cardPath").value.toString(),
                            "false"
                        )
                    )
                }
                list.value = cardList
            }
            .addOnFailureListener {}
        return list
    }
}