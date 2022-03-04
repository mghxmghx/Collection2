package com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class GeneralAnalysisViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase) :
    ViewModel() {
    companion object {
        const val CARDS = "cards"
        const val CARD_NAME = "cardName"
        const val CARD_VOTE_COUNT = "voteCount"
        const val CARD_AVERAGE = "cardAverage"
        const val CARD_PRICE = "cardPrice"
        const val CARD_PATH = "cardPath"
    }
    fun getBeers(): MutableLiveData<List<GeneralAnalysisModel>> {
        val returnList = MutableLiveData<List<GeneralAnalysisModel>>()
        val list = arrayListOf<GeneralAnalysisModel>()
        firebaseDatabase.getReference(CARDS).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(
                    GeneralAnalysisModel(
                        cardName = child.child(CARD_NAME).value.toString(),
                        voteCount = child.child(CARD_VOTE_COUNT).value.toString(),
                        average = child.child(CARD_AVERAGE).value.toString(),
                        price = child.child(CARD_PRICE).value.toString(),
                        cardPath = child.child(CARD_PATH).value.toString()
                    )
                )
            }
            returnList.value = list
        }
        return returnList
    }
}