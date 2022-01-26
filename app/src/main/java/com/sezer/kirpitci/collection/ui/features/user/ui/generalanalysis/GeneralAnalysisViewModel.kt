package com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class GeneralAnalysisViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase): ViewModel() {
    fun getBeers(): MutableLiveData<List<GeneralAnalysisModel>> {
        val returnList = MutableLiveData<List<GeneralAnalysisModel>>()
        val list = arrayListOf<GeneralAnalysisModel>()
        firebaseDatabase.getReference("cards").get().addOnSuccessListener {
            for (child in it.children) {
                list.add(
                    GeneralAnalysisModel(
                        cardName = child.child("cardName").getValue().toString(),
                        voteCount = child.child("voteCount").getValue().toString(),
                        average = child.child("cardAverage").getValue().toString(),
                        price = child.child("cardPrice").getValue().toString(),
                        cardPath = child.child("cardPath").getValue().toString()
                    )
                )
            }
            returnList.value = list
        }
        return returnList
    }

}