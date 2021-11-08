package com.sezer.kirpitci.collection.ui.features.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {
    val auth= FirebaseAuth.getInstance()
    fun auth(id:String,password:String) : MutableLiveData<Boolean> {
        val isSuccess=MutableLiveData<Boolean>()


            if (!id.isEmpty() && !password.isEmpty())
            {
                auth.signInWithEmailAndPassword(id,password).addOnCompleteListener{


                    if(it.isSuccessful)
                   {
                        isSuccess.value=it.isSuccessful
                   }
                    else
                    {
                        isSuccess.value=it.isSuccessful
                   }
                }
            }
            else
            {
                isSuccess.value=false
            }
        return isSuccess

    }
    fun getStatus():MutableLiveData<String>{
        val personStatues=MutableLiveData<String>()
        val db=FirebaseDatabase.getInstance()
        val db2=db.getReference("users")

        db2.get()

            .addOnSuccessListener {
                for (child in it.children)
                {

                    if(child.child("email").getValue().toString().equals(auth.currentUser?.email))
                    {
                        personStatues.value=child.child("status").getValue().toString()

                    }
                }

            }

        return personStatues
    }

}