package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import javax.inject.Inject

class BeerFragmentViewModel @Inject constructor(
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

    fun getCards(category: String, userID: String): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child("cardCategory").value.toString().equals(category)) {
                    list.add(
                        CardModel(
                            cardID = child.child("cardID").value.toString(),
                            cardName = child.child("cardName").value.toString(),
                            cardInfo = child.child("cardInfo").value.toString(),
                            cardCategory = child.child("cardCategory").value.toString(),
                            cardCounty = child.child("cardCounty").value.toString(),
                            cardCity = child.child("cardCity").value.toString(),
                            cardPrice = child.child("cardPrice").value.toString(),
                            cardPath = child.child("cardPath").value.toString(),
                            cardAverage = child.child("cardAverage").value.toString(),
                            child.child("users").child(userID).child("userCardStatus").value
                                .toString(),
                            child.child("users").child(userID).child("userStarRate").value
                                .toString(),
                            voteCount = child.child("voteCount").value.toString(),
                            cardCompany = child.child("cardCompany").value.toString(),
                            cardABV = child.child("cardABV").value.toString(),

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
                    if (child.child("cardName").value.toString().toLowerCase()
                            .contains(alcoholName.toLowerCase())
                    ) {
                        list.add(
                            CardModel(
                                cardID = child.child("cardID").value.toString(),
                                cardName = child.child("cardName").value.toString(),
                                cardInfo = child.child("cardInfo").value.toString(),
                                cardCategory = child.child("cardCategory").value.toString(),
                                cardCounty = child.child("cardCounty").value.toString(),
                                cardCity = child.child("cardCity").value.toString(),
                                cardPrice = child.child("cardPrice").value.toString(),
                                cardPath = child.child("cardPath").value.toString(),
                                child.child("cardStarAverage").value.toString(),
                                child.child("users").child(userID).child("userCardStatus")
                                    .value.toString(),
                                child.child("users").child(userID).child("userStarRate").value
                                    .toString(),
                                cardCompany = child.child("cardCompany").value.toString(),
                                cardABV = child.child("cardABV").value.toString()
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
                                        .setValue(checked.toString()).addOnCompleteListener {
                                            isSuccess.value = it.isSuccessful
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