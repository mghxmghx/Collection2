package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.admin.addcard.AdminAddCardViewModel.Companion.CATEGORIES
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.others.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class BeerFragmentViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val auth: FirebaseAuth,
    val service: RetrofitService
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
        const val COMMENTS = "comments"
        const val COMMENT = "comment"
        const val COMMENT_USER = "commentUser"
        const val COMMENT_TIME = "commentTime"
        const val CUR_RUB = "RUB"
        const val BEER_IN_COUNTRY = "beerInCountry"
        const val BEER_ML = "beerML"
        const val CARD_TYPE = "cardType"
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
    fun getCardComments(id: String): MutableLiveData<List<CommentModel>> {
        val returnList = MutableLiveData<List<CommentModel>>()
        val list = arrayListOf<CommentModel>()
        firebaseDatabase.getReference(CARDS).child(id).child(COMMENTS).get()
            .addOnSuccessListener {
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

    fun getConvertedValue(from: String, to: String): MutableLiveData<String> {
        var newFrom = from
        var newTo = to
        if (from == CUR_RUB) {
            newFrom = to
            newTo = from
        }
        val value = MutableLiveData<String>()
        service.getExchangeCurrency(newFrom, newTo, "1")?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                val res = response.body()
                value.value = res
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
        return value
    }

    fun getCategoryList(): MutableLiveData<List<String>> {
        val list = arrayListOf<String>()
        val returnList = MutableLiveData<List<String>>()
        firebaseDatabase.getReference(CATEGORIES).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(child.value.toString())
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
                            userVoted = child.child(USERS).child(userID).child("userVoted")
                                .value.toString(),
                            cardCompany = child.child(CARD_COMPANY).value.toString(),
                            cardABV = child.child(CARD_ABV).value.toString(),
                            beerML = child.child(BEER_ML).value.toString(),
                            cardType = child.child(CARD_TYPE).value.toString()

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
                                cardABV = child.child(CARD_ABV).value.toString(),
                                beerML = child.child(BEER_ML).value.toString(),
                                cardType = child.child(CARD_TYPE).value.toString()

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

    fun getCountryList(): MutableLiveData<List<String>> {
        val list = arrayListOf<String>()
        val returnList = MutableLiveData<List<String>>()
        firebaseDatabase.getReference("countries").get().addOnSuccessListener {
            for (child in it.children) {
                list.add(child.value.toString())
            }
            returnList.value = list
        }
        return returnList
    }

    fun getTopSheetSearchList(
        country: String, minStar: String, maxStar: String, userID: String,
        beerType: String, userCardStatus: String,
        minPrice: Float, maxPrice: Float
    ): MutableLiveData<List<CardModel>> {
        val list = arrayListOf<CardModel>()
        val returnList = MutableLiveData<List<CardModel>>()
        firebaseDatabase.getReference(CARDS).get().addOnSuccessListener {
            for (child in it.children) {
                var average: Float = 0F
                val vote = child.child(CARD_VOTE_COUNT).value.toString().toFloat()
                val cardAverage = child.child(CARD_AVERAGE).value.toString().toFloat()
                val cardPrice = child.child(CARD_PRICE).value.toString().split(" ").get(0).toString().toFloat()
                if (vote > 0) {
                    average = cardAverage / vote
                } else {
                    average = 0F
                }
                if (country != "All") {
                    if (beerType != "All") {
                        if (child.child("cardCounty").value.toString()
                                .equals(country) && average >= minStar.toFloat() &&
                            average <= maxStar.toFloat() && child.child("cardCompany").value.toString()
                                .equals(beerType) &&
                            child.child(USERS).child(userID)
                                .child(CARD_USER_STATUS).value.toString().equals(userCardStatus) &&
                            cardPrice >= minPrice && cardPrice <= maxPrice
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
                                    userVoted = child.child(USERS).child(userID).child("userVoted")
                                        .value.toString(),
                                    cardCompany = child.child(CARD_COMPANY).value.toString(),
                                    cardABV = child.child(CARD_ABV).value.toString(),
                                    beerML = child.child(BEER_ML).value.toString(),
                                    cardType = child.child(CARD_TYPE).value.toString()

                                )
                            )
                        }
                    } else {
                        if (child.child("cardCounty").value.toString()
                                .equals(country) && average >= minStar.toFloat() &&
                            average <= maxStar.toFloat() &&
                            child.child(USERS).child(userID)
                                .child(CARD_USER_STATUS).value.toString().equals(userCardStatus) &&
                            child.child(CARD_PRICE).value.toString()
                                .toFloat() >= minPrice && child.child(
                                CARD_PRICE
                            ).value.toString().toFloat() <= maxPrice
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
                                    userVoted = child.child(USERS).child(userID).child("userVoted")
                                        .value.toString(),
                                    cardCompany = child.child(CARD_COMPANY).value.toString(),
                                    cardABV = child.child(CARD_ABV).value.toString(),
                                    beerML = child.child(BEER_ML).value.toString(),
                                    cardType = child.child(CARD_TYPE).value.toString()

                                )
                            )
                        }

                    }

                } else {
                    if (beerType != "All") {
                        if (average >= minStar.toFloat() &&
                            average <= maxStar.toFloat() && child.child("cardCompany").value.toString()
                                .equals(beerType) &&
                            child.child(USERS).child(userID)
                                .child(CARD_USER_STATUS).value.toString().equals(userCardStatus) &&
                            child.child(CARD_PRICE).value.toString()
                                .toFloat() >= minPrice && child.child(
                                CARD_PRICE
                            ).value.toString().toFloat() <= maxPrice
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
                                    userVoted = child.child(USERS).child(userID).child("userVoted")
                                        .value.toString(),
                                    cardCompany = child.child(CARD_COMPANY).value.toString(),
                                    cardABV = child.child(CARD_ABV).value.toString(),
                                    beerML = child.child(BEER_ML).value.toString()
                                )
                            )
                        }
                    } else {
                        if (average >= minStar.toFloat() &&
                            average <= maxStar.toFloat() &&
                            child.child(USERS).child(userID)
                                .child(CARD_USER_STATUS).value.toString().equals(userCardStatus) &&
                            child.child(CARD_PRICE).value.toString().split(" ").get(0).toString()
                                .toFloat() >= minPrice && child.child(
                                CARD_PRICE
                            ).value.toString().split(" ").get(0).toString().toFloat() <= maxPrice
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
                                    userVoted = child.child(USERS).child(userID).child("userVoted")
                                        .value.toString(),
                                    cardCompany = child.child(CARD_COMPANY).value.toString(),
                                    cardABV = child.child(CARD_ABV).value.toString(),
                                    beerML = child.child(BEER_ML).value.toString(),
                                    cardType = child.child(CARD_TYPE).value.toString()


                                )
                            )
                        }
                    }

                }

            }
            returnList.value = list
        }
        return returnList
    }
}