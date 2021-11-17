package com.sezer.kirpitci.collection.ui.features.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserViewModel:ViewModel() {
    val auth= FirebaseAuth.getInstance()

    fun getMyCards(){
        Log.d("tag", "getMyCards: ")
        Log.d("TAG", "getMyCards: " + auth.currentUser?.email)
        val personStatues=MutableLiveData<String>()
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
                    }
                    for(i in 0..child.child("cards").childrenCount){
                        Log.d("TAG", "getMyCards: " + child.child("cards").children)
                    }

                }
            }
        }.addOnFailureListener {
        }
        }
    }