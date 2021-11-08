package com.sezer.kirpitci.collection.ui.features.admin.viewcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class AdminViewCardViewModel:ViewModel() {

    fun getCards():MutableLiveData<List<ViewCardModel>> {

        val list = MutableLiveData<List<ViewCardModel>>()
        val cardList = ArrayList<ViewCardModel>()
        val db = FirebaseDatabase.getInstance()
        val db2 = db.getReference("cards")

        db2.get()

            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(ViewCardModel(child.child("cardID").getValue().toString(),
                        child.child("cardName").getValue().toString(),
                        child.child("cardInfo").getValue().toString(),
                        child.child("cardCategory").getValue().toString(),
                        child.child("cardCounty").getValue().toString(),
                        child.child("cardCity").getValue().toString(),
                        child.child("cardPrice").getValue().toString(),
                        child.child("cardPath").getValue().toString(),
                    ))
//r cardID:String,var cardName:String, var cardInfo:String?, var cardCategory:String, var cardCounty:String, var cardCity:String?, var cardPrice:String?, var cardPath:String
                }
                list.value=cardList


            }

        return list
    }
}