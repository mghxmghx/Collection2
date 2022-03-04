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
    companion object {
        const val CARD_ID = "cardID"
        const val CARD_NAME = "cardName"
        const val CARD_INFO = "cardInfo"
        const val CARD_CATEGORY = "cardCategory"
        const val CARD_COUNTRY = "cardCounty"
        const val CARD_CITY = "cardCity"
        const val CARD_PRICE = "cardPrice"
        const val CARD_PATH = "cardPath"
        const val CARD_VOTE_COUNT = "voteCount"
        const val CARD_USER_RATE = "userStarRate"
        const val CARD_USER_VOTED = "userVoted"
        const val CARD_AVERAGE = "cardAverage"
        const val CARD_USER_STATUS = "userCardStatus"
        const val CARD_COMPANY = "cardCompany"
        const val CARD_ABV = "cardABV"
        const val CARDS = "cards"
        const val DEFAULT = "default"
        const val CARDSV2 = "Cards"
        const val USERS = "users"
        const val EMAIL = "email"
        const val CARD_STAR_AVERAGE = "cardStarAverage"
    }
    fun getUserID(): MutableLiveData<String> {
        val userID = MutableLiveData<String>()
        firebaseDatabase.getReference(USERS).get().addOnSuccessListener {
            for (i in it.children) {
                if (i.child(EMAIL).value.toString()
                        .equals(auth.currentUser?.email.toString())
                ) {
                    userID.value = i.key.toString()
                }
            }
        }
        return userID
    }

    fun getCards(category: String, userID: String, language: String): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference(CARDS)
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child(CARD_CATEGORY).value.toString().equals(category) && child.child("beerInCountry").value.toString().contains(language)) {
                    list.add(
                        CardModel(
                            child.child(CARD_ID).value.toString(),
                            child.child(CARD_NAME).value.toString(),
                            child.child(CARD_INFO).value.toString(),
                            child.child(CARD_CATEGORY).value.toString(),
                            child.child(CARD_COUNTRY).value.toString(),
                            child.child(CARD_CITY).value.toString(),
                            child.child(CARD_PRICE).value.toString(),
                            child.child(CARD_PATH).value.toString(),
                            child.child(CARD_AVERAGE).value.toString(),
                            child.child(USERS).child(userID).child(CARD_USER_STATUS).value
                                .toString(),
                            child.child(USERS).child(userID).child(CARD_USER_RATE).value
                                .toString(),
                            voteCount = child.child(CARD_VOTE_COUNT).value.toString(),
                            userVoted = child.child(USERS).child(userID).child(CARD_USER_VOTED)
                                .value.toString(),
                            cardCompany = child.child(CARD_COMPANY).value.toString(),
                            cardABV = child.child(CARD_ABV).value.toString()

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
        var db2 = firebaseDatabase.getReference(CARDS)
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child(CARD_CATEGORY).value.toString().equals(category)) {
                    if (child.child(CARD_NAME).value.toString().toLowerCase()
                            .contains(alcoholName.toLowerCase())
                    ) {
                        list.add(
                            CardModel(
                                cardID = child.child(CARD_ID).value.toString(),
                                cardName = child.child(CARD_NAME).value.toString(),
                                cardInfo = child.child(CARD_INFO).value.toString(),
                                cardCategory = child.child(CARD_CATEGORY).value.toString(),
                                cardCounty = child.child(CARD_COUNTRY).value.toString(),
                                cardCity = child.child(CARD_CITY).value.toString(),
                                cardPrice = child.child(CARD_PRICE).value.toString(),
                                cardPath = child.child(CARD_PATH).value.toString(),
                                child.child(CARD_STAR_AVERAGE).value.toString(),
                                child.child(USERS).child(userID).child(CARD_USER_STATUS)
                                    .value.toString(),
                                child.child(USERS).child(userID).child(CARD_USER_RATE).value
                                    .toString(),
                                cardCompany = child.child(CARD_COMPANY).value.toString(),
                                cardABV = child.child(CARD_ABV).value.toString()
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
        firebaseDatabase.getReference(CARDS).get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child(CARD_ID).value.toString().equals(model.cardID)) {
                    firebaseDatabase.getReference(CARDS).child(child.key.toString())
                        .child(USERS).get().addOnSuccessListener {
                            for (i in it.children) {
                                if (i.key.toString().equals(userID)) {
                                    firebaseDatabase.getReference(CARDS).child(
                                        child
                                            .key.toString()
                                    ).child(USERS).child(userID).child(CARD_USER_STATUS)
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