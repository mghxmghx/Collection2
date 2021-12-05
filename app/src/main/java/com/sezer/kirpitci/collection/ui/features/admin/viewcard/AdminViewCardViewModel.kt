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
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminViewCardViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase) : ViewModel() {
    var quote: MutableLiveData<List<ViewCardModel>>? = getCards()
    fun getCards(): MutableLiveData<List<ViewCardModel>> {

        val list = MutableLiveData<List<ViewCardModel>>()
        val cardList = ArrayList<ViewCardModel>()

        val db2 = firebaseDatabase.getReference("cards")
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        ViewCardModel(
                            child.child("cardID").value.toString(),
                            child.child("cardName").value.toString(),
                            child.child("cardInfo").value.toString(),
                            child.child("cardCategory").value.toString(),
                            child.child("cardCounty").value.toString(),
                            child.child("cardCity").value.toString(),
                            child.child("cardPrice").value.toString(),
                            child.child("cardPath").value.toString(),
                        )
                    )
                }
                list.value = cardList
            }
            .addOnFailureListener {}
        return list
    }

    fun deleteChildren(model: ViewCardModel) {
        val reqestDB = firebaseDatabase.getReference("cards")
        reqestDB.child(model.cardID.toString()).removeValue().addOnCompleteListener {
            Log.d("TAG", "deleteChildren: " + it.isSuccessful)
            Log.d("TAG", "deleteChildren: " + it.isComplete)
        }
    }

    fun setChildImage(filePath: Uri, imageID: String): MutableLiveData<String> {
        val isSuccess = MutableLiveData<String>()
        if (!filePath.toString().equals("default")) {
            val storageReference = FirebaseStorage.getInstance().getReference("Cards")
            val ref = storageReference.child("uploads/" + UUID.randomUUID().toString())
            val db = FirebaseFirestore.getInstance()
            val uploadTask = ref.putFile(filePath)
            val urlTask = uploadTask.continueWithTask(
                Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it

                        }
                    }
                    return@Continuation ref.downloadUrl
                }
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val data = HashMap<String, Any>()
                    val information = imageID

                    data[information] = downloadUri.toString()
                    db.collection("posts")
                        .add(data)
                        .addOnSuccessListener {
                            isSuccess.value = task.result.toString()
                        }
                        .addOnFailureListener {
                            isSuccess.value = "default"
                        }
                } else {
                    isSuccess.value = "default"
                }
            }
        } else {
            isSuccess.value = "default"
        }
        return isSuccess
    }

    fun updateCard(newModel: ViewCardModel): MutableLiveData<String> {
        val reqestDB = firebaseDatabase.getReference("cards")
        val isSuccess = MutableLiveData<String>()
        reqestDB.child(newModel.cardID).setValue(newModel).addOnCompleteListener {
            Log.d("TAG", "updateCard: " + it.isSuccessful)
            if (it.isSuccessful) {
                isSuccess.value = it.isSuccessful.toString()
            } else {
                isSuccess.value = "default"
            }
        }
        return isSuccess
    }

}