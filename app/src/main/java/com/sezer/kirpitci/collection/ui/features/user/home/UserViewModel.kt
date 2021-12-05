package com.sezer.kirpitci.collection.ui.features.user.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import javax.inject.Inject

class UserViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth) : ViewModel() {
    fun getMyCards(): MutableLiveData<List<CardModel>> {
        Log.d("TAG", "getMyCards:1 ")
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("users")
        Log.d("TAG", "getMyCards: 1.2")
        db2.get().addOnSuccessListener {
            Log.d("TAG", "getMyCards: 2")
            for (child in it.children) {
                Log.d("TAG", "getMyCards: 3 " )
                if (child.child("email").value.toString().equals(auth.currentUser?.email)) {
                    Log.d("TAG", "getMyCards: 4" )
                    db2 = firebaseDatabase.getReference("users").child(child.key.toString()).child("cards")
                    Log.d("TAG", "getMyCards:5 ")
                    db2.get().addOnSuccessListener {
                        Log.d("TAG", "getMyCards: 6")
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