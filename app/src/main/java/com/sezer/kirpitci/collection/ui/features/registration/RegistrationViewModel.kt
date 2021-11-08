package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationViewModel:ViewModel() {

    val auth=FirebaseAuth.getInstance()
    fun createUser(model:CreateUserModel):MutableLiveData<Boolean>{
        val isSuccess=MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(model.email,model.password).addOnCompleteListener{


            if (it.isSuccessful){
                isSuccess.value=it.isSuccessful

            }
            else {
                isSuccess.value=false
            }
        }

        return isSuccess
    }
    fun createStatus(model:AddUserModel):MutableLiveData<Boolean>{

        val isSuccess= MutableLiveData<Boolean>()
        val db= FirebaseDatabase.getInstance().getReference("users")
        db.child(model.userName.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value=it.isSuccessful



        }

        return isSuccess

    }
}