package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import javax.inject.Inject

class BeerFragmentViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth): ViewModel() {
    fun getMyCards(): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("users")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("email").value.toString().equals(auth.currentUser?.email)) {
                    db2 = firebaseDatabase.getReference("users").child(child.key.toString()).child("cards")
                    db2.get().addOnSuccessListener {
                        for(i in it.children){
                            list.add(
                                CardModel(
                                    i.child("cardID").getValue().toString(),
                                    i.child("cardName").getValue().toString(),
                                    i.child("cardInfo").getValue().toString(),
                                    i.child("cardCategory").getValue().toString(),
                                    i.child("cardCounty").getValue().toString(),
                                    i.child("cardCity").getValue().toString(),
                                    i.child("cardPrice").getValue().toString(),
                                    i.child("cardPath").getValue().toString(),
                                    i.child("status").getValue().toString(),
                                )
                            )
                        }
                        cardList.value = list
                    }
                }
            }
        }.addOnFailureListener {
        }
        return cardList
    }
}