package com.sezer.kirpitci.collection.ui.features.user.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.user.home.CurrentCardList

class BeerFragmentViewModel: ViewModel() {
    val auth= FirebaseAuth.getInstance()
    fun getMyCards(): MutableLiveData<List<CurrentCardList>> {
        val cardList = MutableLiveData<List<CurrentCardList>>()
        val list = arrayListOf<CurrentCardList>()
        val db= FirebaseDatabase.getInstance()
        val db2=db.getReference("users")
        db2.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                if (child.child("email").getValue().toString().equals(auth.currentUser?.email)) {
                    child.child("cards").children.forEach {
                        val item = it.value.toString()
                        val splitItem = item.split(",")
                        val itemOne = splitItem[0].substring(8)
                        val itemTwo = splitItem[1].substring(8,splitItem[1].length-1)
                        list.add(CurrentCardList(itemOne, itemTwo))
                    }
                    cardList.value = list
                }
            }
        }.addOnFailureListener {
        }
        return cardList
    }

    fun getCardInformation(currentList: List<CurrentCardList>, category: String): MutableLiveData<List<ViewCardStatusModel>> {
        val cardList = MutableLiveData<List<ViewCardStatusModel>>()
        val list = arrayListOf<ViewCardStatusModel>()
        val db= FirebaseDatabase.getInstance()
        val db2=db.getReference("cards")
        db2.get().addOnSuccessListener {
            for (child in it.getChildren()) {
                for(i in 0..currentList.size-1){
                    if(child.child("cardID").getValue().toString().equals(currentList.get(i).cardID)){
                        if(child.child("cardCategory").getValue().toString().equals(category)) {
                            list.add(
                                ViewCardStatusModel(child.child("cardID").getValue().toString(),
                                child.child("cardName").getValue().toString(),
                                child.child("cardInfo").getValue().toString(),
                                child.child("cardCategory").getValue().toString(),
                                child.child("cardCounty").getValue().toString(),
                                child.child("cardCity").getValue().toString(),
                                child.child("cardPrice").getValue().toString(),
                                child.child("cardPath").getValue().toString(),
                                currentList.get(i).cardCurrentStatus
                            )
                            )
                        }
                    }
                }
            }
            cardList.value = list
        }
        return cardList
    }
}