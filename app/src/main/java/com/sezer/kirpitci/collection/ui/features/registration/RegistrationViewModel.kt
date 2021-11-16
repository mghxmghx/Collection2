package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel

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
                            child.child("cardID").getValue().toString(),"false"

                        )
                    )
//r cardID:String,var cardName:String, var cardInfo:String?, var cardCategory:String, var cardCounty:String, var cardCity:String?, var cardPrice:String?, var cardPath:String
                }
                list.value=cardList


            }
            .addOnFailureListener{}
        return list
    }
}