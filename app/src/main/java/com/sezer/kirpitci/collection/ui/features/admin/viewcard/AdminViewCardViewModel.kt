package com.sezer.kirpitci.collection.ui.features.admin.viewcard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminViewCardViewModel:ViewModel() {
    var quote : MutableLiveData<List<ViewCardModel>>? = getCards()
    fun getCards():MutableLiveData<List<ViewCardModel>> {

        val list = MutableLiveData<List<ViewCardModel>>()
        val cardList = ArrayList<ViewCardModel>()

        val db = FirebaseDatabase.getInstance()
        val db2 = db.getReference("cards")

        db2.get()

            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        ViewCardModel(
                            child.child("cardID").getValue().toString(),
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
            .addOnFailureListener{}
        return list
    }
    fun deleteChildren(model: ViewCardModel) {
        Log.d("TAG", "getData: "+model.cardID)
        val db = FirebaseDatabase.getInstance()
        val reqestDB = db.getReference("cards")
        reqestDB.child(model.cardID.toString()).removeValue().addOnCompleteListener{
            Log.d("TAG", "deleteChildren: "+it.isSuccessful)
            Log.d("TAG", "deleteChildren: "+it.isComplete)
        }


    }

    fun setChildImage(filePath: Uri, imageID:String):MutableLiveData<String>
    {
        val isSuccess=MutableLiveData<String>()

        if(!filePath.toString().equals("default"))
        {
            val storageReference= FirebaseStorage.getInstance().getReference("Cards")
            val ref=storageReference.child("uploads/"+ UUID.randomUUID().toString())
            val db= FirebaseFirestore.getInstance()
            val uploadTask=ref.putFile(filePath)
            val urlTask=uploadTask.continueWithTask(
                Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                        task ->
                    if(!task.isSuccessful){
                        task.exception?.let {
                            throw it

                        }
                    }
                    return@Continuation ref.downloadUrl

                }

            ).addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    val downloadUri=task.result
                    val data=HashMap<String,Any>()
                    val information=imageID

                    data[information]=downloadUri.toString()
                    db.collection("posts")
                        .add(data)
                        .addOnSuccessListener {
                            isSuccess.value= task.result.toString()

                        }
                        .addOnFailureListener{
                            isSuccess.value= "default"
                        }

                }
                else{
                    isSuccess.value="default"

                }
            }
        }
        else{
            isSuccess.value="default"

        }
        return isSuccess
    }

    fun updateCard(newModel: ViewCardModel): MutableLiveData<String> {

        val db = FirebaseDatabase.getInstance()
        val reqestDB = db.getReference("cards")
        val isSuccess=MutableLiveData<String>()
        reqestDB.child(newModel.cardID).setValue(newModel).addOnCompleteListener{
            Log.d("TAG", "updateCard: "+it.isSuccessful)
            if(it.isSuccessful){
                isSuccess.value=it.isSuccessful.toString()

            }
            else
            {
                isSuccess.value="default"
            }


        }
        return isSuccess
    }

}