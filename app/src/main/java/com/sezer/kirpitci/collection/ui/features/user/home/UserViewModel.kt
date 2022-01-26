package com.sezer.kirpitci.collection.ui.features.user.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import java.util.*
import javax.inject.Inject

class UserViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth
) : ViewModel() {
    fun getUserID(): MutableLiveData<String> {
        val userID = MutableLiveData<String>()
        firebaseDatabase.getReference("users").get().addOnSuccessListener {
            for (i in it.children) {
                if (i.child("email").value.toString()
                        .equals(auth.currentUser?.email.toString())
                ) {
                    userID.value = i.key.toString()
                }
            }
        }
        return userID
    }

    fun setStarInFB(model: CardModel, userID: String, oldVote: String?, newVote: String) {
        setAverage(model, oldVote, newVote)
        firebaseDatabase.getReference("cards").child(model.cardID).child("voteCount").setValue(model.voteCount).addOnSuccessListener {
            firebaseDatabase.getReference("cards").get().addOnSuccessListener {
                for (child in it.children) {
                    if (child.child("cardID").value.toString().equals(model.cardID)) {
                        firebaseDatabase.getReference("cards").child(child.key.toString())
                            .child("users").get().addOnSuccessListener {
                                for (i in it.children) {

                                    if (i.key.toString().equals(userID)) {
                                        firebaseDatabase.getReference("cards").child(
                                            child
                                                .key.toString()
                                        ).child("users").child(userID).child("userStarRate")
                                            .setValue(model.userStarRate)
                                        firebaseDatabase.getReference("cards").child(
                                            child
                                                .key.toString()
                                        ).child("users").child(userID).child("userVoted")
                                            .setValue(model.userVoted)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
    private fun compareOldNew(oldVote: String?, newVote: String): Int {
        var vote = 0
        if(oldVote.equals("null")){
            vote = newVote.toInt()
        } else {
            vote = newVote.toInt() - oldVote.toString().toInt()
        }
        return vote
    }

    private fun setAverage(model: CardModel, oldVote: String?, newVote: String){
       val diff =  compareOldNew(oldVote, newVote)
        firebaseDatabase.getReference("cards").child(model.cardID).get().addOnSuccessListener {
           val cardAverage = it.child("cardAverage").getValue().toString().toFloat() //6
            val newRate = cardAverage+diff
            firebaseDatabase.getReference("cards").child(model.cardID).child("cardAverage").setValue(newRate.toString())
        }
    }

    fun getCards(category: String, userID: String): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardCategory").value.toString().equals(category)) {
                    list.add(
                        CardModel(
                            child.child("cardID").value.toString(),
                            child.child("cardName").value.toString(),
                            child.child("cardInfo").value.toString(),
                            child.child("cardCategory").value.toString(),
                            child.child("cardCounty").value.toString(),
                            child.child("cardCity").value.toString(),
                            child.child("cardPrice").value.toString(),
                            child.child("cardPath").value.toString(),
                            child.child("cardAverage").value.toString(),
                            child.child("users").child(userID).child("userCardStatus").value
                                .toString(),
                            child.child("users").child(userID).child("userStarRate").value
                                .toString(),
                            voteCount = child.child("voteCount").getValue().toString(),
                            userVoted = child.child("users").child(userID).child("userVoted").getValue().toString()
                        )
                    )
                }

            }
            cardList.value = list
        }
        return cardList
    }

    fun searchCards(
        alcoholName: String,
        category: String,
        userID: String
    ): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardCategory").value.toString().equals(category)) {
                    if (child.child("cardName").value.toString().lowercase(Locale.getDefault())
                            .contains(alcoholName.lowercase(Locale.getDefault()))
                    ) {
                        list.add(
                            CardModel(
                                child.child("cardID").value.toString(),
                                child.child("cardName").value.toString(),
                                child.child("cardInfo").value.toString(),
                                child.child("cardCategory").value.toString(),
                                child.child("cardCounty").value.toString(),
                                child.child("cardCity").value.toString(),
                                child.child("cardPrice").value.toString(),
                                child.child("cardPath").value.toString(),
                                child.child("cardStarAverage").value.toString(),
                                child.child("users").child(userID).child("userCardStatus")
                                    .value.toString(),
                                child.child("users").child(userID).child("userStarRate").value
                                    .toString()
                            )
                        )
                    }

                }

            }
            cardList.value = list
        }
        return cardList
    }

    fun setCheck(checked: Boolean, model: CardModel, userID: String): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        firebaseDatabase.getReference("cards").get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardID").value.toString().equals(model.cardID)) {
                    firebaseDatabase.getReference("cards").child(child.key.toString())
                        .child("users").get().addOnSuccessListener {
                        for (i in it.children) {
                            if (i.key.toString().equals(userID)) {
                                firebaseDatabase.getReference("cards").child(
                                    child
                                        .key.toString()
                                ).child("users").child(userID).child("userCardStatus")
                                    .setValue(checked.toString()).addOnSuccessListener {
                                    isSuccess.value = true
                                }
                            }
                        }
                    }
                }
            }
        }
        return isSuccess
    }
}