package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import javax.inject.Inject
import kotlin.math.log

class BeerFragmentViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase, val auth: FirebaseAuth): ViewModel() {
    fun getCards(category: String): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardCategory").getValue().toString().equals(category)){
                    val usermail = auth.currentUser?.email?.split("@")?.get(0)
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
                            child.child("users").child(usermail.toString()).child("status").getValue().toString(),
                        )
                    )
            }
            }
            cardList.value = list
        }
        return cardList
    }
    fun setCheck(checked: Boolean, model: CardModel) {
        val usermail = auth.currentUser?.email.toString().split("@")
        firebaseDatabase.getReference("cards").get().addOnSuccessListener {
            for(child in it.children){
                if(child.child("cardID").getValue().toString().equals(model.cardID)){
                    firebaseDatabase.
                    getReference("cards").
                    child(child.key.toString()).
                    child("users").
                    get().addOnSuccessListener {
                            for(i in it.children){
                                if(i.child("userMail").getValue().toString().equals(usermail.get(0))){
                                    firebaseDatabase.getReference("cards").child(child
                                        .key.toString()).child("users").child(i.key.toString()).child("status").
                                            setValue(checked.toString())
                                }
                            }
                        }
                }
            }
        }
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