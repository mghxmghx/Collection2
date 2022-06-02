package com.sezer.kirpitci.collection.ui.features.user.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.CommentModel
import java.util.*
import javax.inject.Inject

class UserViewModel @Inject constructor(
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
        const val COMMENTS = "comments"
        const val COMMENT = "comment"
        const val COMMENT_USER = "commentUser"
        const val COMMENT_TIME = "commentTime"
        const val BEER_IN_COUNTRY = "beerInCountry"
        const val BEER_ML = "beerML"
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

    fun setStarInFB(model: CardModel, userID: String, oldVote: String?, newVote: String) {
        setAverage(model, oldVote, newVote)
        firebaseDatabase.getReference(CARDS).child(model.cardID).child(CARD_VOTE_COUNT)
            .setValue(model.voteCount).addOnSuccessListener {
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
                                            ).child(USERS).child(userID).child(CARD_USER_RATE)
                                                .setValue(model.userStarRate)
                                            firebaseDatabase.getReference(CARDS).child(
                                                child
                                                    .key.toString()
                                            ).child(USERS).child(userID).child(CARD_USER_VOTED)
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
        if (oldVote.equals("null")) {
            vote = newVote.toInt()
        } else {
            vote = newVote.toInt() - oldVote.toString().toInt()
        }
        return vote
    }

    private fun setAverage(model: CardModel, oldVote: String?, newVote: String) {
        val diff = compareOldNew(oldVote, newVote)
        firebaseDatabase.getReference(CARDS).child(model.cardID).get().addOnSuccessListener {
            val cardAverage = it.child(CARD_AVERAGE).value.toString().toFloat() //6
            val newRate = cardAverage + diff
            firebaseDatabase.getReference(CARDS).child(model.cardID).child(CARD_AVERAGE)
                .setValue(newRate.toString())
        }
    }

    fun getCardDetails(id: String, userID: String): MutableLiveData<CardModel> {
        val model = MutableLiveData<CardModel>()
        firebaseDatabase.getReference(CARDS).child(id).get().addOnSuccessListener {
            model.value = CardModel(
                it.child(CARD_ID).value.toString(),
                it.child(CARD_NAME).value.toString(),
                it.child(CARD_INFO).value.toString(),
                it.child(CARD_CATEGORY).value.toString(),
                it.child(CARD_COUNTRY).value.toString(),
                it.child(CARD_CITY).value.toString(),
                it.child(CARD_PRICE).value.toString(),
                it.child(CARD_PATH).value.toString(),
                cardAverage = it.child(CARD_AVERAGE).value.toString(),
                it.child(USERS).child(userID).child(CARD_USER_STATUS).value
                    .toString(),
                it.child(USERS).child(userID).child(CARD_USER_RATE).value
                    .toString(),
                voteCount = it.child(CARD_VOTE_COUNT).value.toString(),
                cardCompany = it.child(CARD_COMPANY).value.toString(),
                cardABV = it.child(CARD_ABV).value.toString(),
                userVoted = it.child(USERS).child(userID).child(CARD_USER_VOTED)
                    .value.toString(),
                beerML = it.child(BEER_ML).value.toString()
            )
        }
        return model
    }

    fun getCardComments(id: String): MutableLiveData<List<CommentModel>> {
        val returnList = MutableLiveData<List<CommentModel>>()
        val list = arrayListOf<CommentModel>()
        firebaseDatabase.getReference(CARDS).child(id).child(COMMENTS).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(
                    CommentModel(
                        comment = child.child(COMMENT).value.toString(),
                        commentUser = child.child(COMMENT_USER).value.toString(),
                        commentTime = child.child(COMMENT_TIME).value.toString()
                    )
                )
            }
            returnList.value = list
        }
        return returnList
    }

    fun getCards(
        category: String,
        userID: String,
        language: String
    ): MutableLiveData<List<CardModel>> {
        Log.d("TAG", "getCards: " + language)
        Log.d("TAG", "getCards: " + category)
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference(CARDS)
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child(CARD_CATEGORY).value.toString()
                        .equals(category) && child.child(BEER_IN_COUNTRY).value.toString()
                        .contains(language)
                ) {
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
                            cardABV = child.child(CARD_ABV).value.toString(),
                            beerML = child.child(BEER_ML).value.toString()

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
                    if (child.child(CARD_NAME).value.toString().lowercase(Locale.getDefault())
                            .contains(alcoholName.lowercase(Locale.getDefault()))
                    ) {
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
                                child.child(USERS).child(userID).child(CARD_USER_STATUS)
                                    .value.toString(),
                                child.child(USERS).child(userID).child(CARD_USER_RATE).value
                                    .toString(),
                                cardCompany = it.child(CARD_COMPANY).value.toString(),
                                cardABV = it.child(CARD_ABV).value.toString(),
                                beerML = child.child(BEER_ML).value.toString()
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

    fun sendComment(model: SendMessageModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        model.commentUser = auth.currentUser?.email?.split("@")?.get(0)
        firebaseDatabase.getReference(CARDS).child(model.cardId).child(COMMENTS)
            .child(model.commentTime).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
        }
        return isSuccess
    }
}