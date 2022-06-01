package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.ui.features.user.home.UserViewModel
import javax.inject.Inject

class DrunkBeerViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase
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
    fun getCards(
        category: String,
        userID: String,
    ): MutableLiveData<List<CardModel>> {
        val cardList = MutableLiveData<List<CardModel>>()
        val list = arrayListOf<CardModel>()
        var db2 = firebaseDatabase.getReference(CARDS)
        db2.get().addOnSuccessListener {
            for (child in it.children) {
                if (child.child(CARD_CATEGORY).value.toString()
                        .equals(category)
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
    fun getData(): MutableLiveData<List<DrunkModel>> {
        val user = auth.currentUser?.email
        val userName = user!!.split("@").get(0)
        val list = MutableLiveData<List<DrunkModel>>()
        val tempList = arrayListOf<DrunkModel>()
        firebaseDatabase.getReference("cards").get().addOnSuccessListener {
            for (child in it.children) {
                tempList.add(
                    DrunkModel(
                        childID = child.key.toString(),
                        beerName = child.child("cardName").value.toString(),
                        beerPhoto = child.child("cardPath").value.toString()
                    )
                )
            }
            list.value = tempList
        }
        return list
    }
    fun getCardDetails(id: String, userID: String): MutableLiveData<CardModel> {
        val model = MutableLiveData<CardModel>()
        firebaseDatabase.getReference(UserViewModel.CARDS).child(id).get().addOnSuccessListener {
            model.value = CardModel(
                it.child(UserViewModel.CARD_ID).value.toString(),
                it.child(UserViewModel.CARD_NAME).value.toString(),
                it.child(UserViewModel.CARD_INFO).value.toString(),
                it.child(UserViewModel.CARD_CATEGORY).value.toString(),
                it.child(UserViewModel.CARD_COUNTRY).value.toString(),
                it.child(UserViewModel.CARD_CITY).value.toString(),
                it.child(UserViewModel.CARD_PRICE).value.toString(),
                it.child(UserViewModel.CARD_PATH).value.toString(),
                cardAverage = it.child(UserViewModel.CARD_AVERAGE).value.toString(),
                it.child(UserViewModel.USERS).child(userID).child(UserViewModel.CARD_USER_STATUS).value
                    .toString(),
                it.child(UserViewModel.USERS).child(userID).child(UserViewModel.CARD_USER_RATE).value
                    .toString(),
                voteCount = it.child(UserViewModel.CARD_VOTE_COUNT).value.toString(),
                cardCompany = it.child(UserViewModel.CARD_COMPANY).value.toString(),
                cardABV = it.child(UserViewModel.CARD_ABV).value.toString(),
                userVoted = it.child(UserViewModel.USERS).child(userID).child(UserViewModel.CARD_USER_VOTED)
                    .value.toString(),
                beerML = it.child(UserViewModel.BEER_ML).value.toString()
            )
        }
        return model
    }
    fun getUserDetail(list: List<DrunkModel>): MutableLiveData<List<DrunkModel>> {
        val user = auth.currentUser?.email
        val userName = user!!.split("@").get(0)
        val listt = MutableLiveData<List<DrunkModel>>()
        val tempList = arrayListOf<DrunkModel>()
        for (i in list.indices) {
            firebaseDatabase.getReference("cards").child(list[i].childID).child("users").get()
                .addOnSuccessListener { userList ->
                    for (child in userList.children) {
                        if (child.child("userEmail").value.toString() == user && child.child("userCardStatus").value.toString() == "true") {
                            tempList.add(
                                DrunkModel(
                                    userStarRate = child.child("userStarRate").value.toString(),
                                    childID = list.get(i).childID,
                                    beerPhoto = list.get(i).beerPhoto,
                                    beerName = list.get(i).beerName
                                )
                            )
                        }
                    }
                }

        }
        listt.value = tempList
        return listt
    }

    fun getCommentDetail() {
  /*      firebaseDatabase.getReference("cards").child(child.key.toString()).child("comments").get()
            .addOnSuccessListener { commentList ->
                val commentLit = arrayListOf<CommentModel>()
                for (childy in commentList.children) {
                    if (childy.child("commentUser").value.toString() == userName) {
                        commentLit.add(
                            CommentModel(
                                comment = childy.child("comment").value.toString(),
                                commentTime = childy.key.toString()
                            )
                        )
                    }
                }
                tempModel.comments = commentLit
                tempList.add(tempModel)
            }*/
    }
}