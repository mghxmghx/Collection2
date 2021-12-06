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
    fun getKey(){

    }
    fun getCards(category:String):  MutableLiveData<List<CardModel>>{
        val userEmail = auth.currentUser?.email
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for(child in it.children){
                if(child.child("cardCategory").getValue().toString().equals(category))
                            list.add(
                                CardModel(
                                    child.child("cardID").getValue().toString(),
                                    child.child("cardName").getValue().toString(),
                                    child.child("cardInfo").getValue().toString(),
                                    child.child("cardCategory").getValue().toString(),
                                    child.child("cardCounty").getValue().toString(),
                                    child.child("cardCity").getValue().toString(),
                                    child.child("cardPrice").getValue().toString(),
                                    child.child("cardPath").getValue().toString(),
                                    child.child("status").getValue().toString(),
                                )
                            )
            }
            cardList.value = list
        }
        return cardList
    }
    fun searchCards(alcoholName: String, category: String):  MutableLiveData<List<CardModel>>{
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardCategory").getValue().toString().equals(category)){
                    if(child.child("cardName").getValue().toString().contains(alcoholName)){
                        list.add(
                            CardModel(
                                child.child("cardID").getValue().toString(),
                                child.child("cardName").getValue().toString(),
                                child.child("cardInfo").getValue().toString(),
                                child.child("cardCategory").getValue().toString(),
                                child.child("cardCounty").getValue().toString(),
                                child.child("cardCity").getValue().toString(),
                                child.child("cardPrice").getValue().toString(),
                                child.child("cardPath").getValue().toString(),
                                child.child("status").getValue().toString(),
                            )
                        )
                    }

                }

            }
            cardList.value = list
        }
        return cardList
    }
}