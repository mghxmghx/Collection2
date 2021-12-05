package com.sezer.kirpitci.collection.ui.features.admin.addcard

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
import kotlin.collections.HashMap


class AdminAddCardViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase,
        val firebaseStorage: FirebaseStorage,
        val firebaseFirestore: FirebaseFirestore) : ViewModel() {
    fun getMaxId(): MutableLiveData<Int> {
        val db = firebaseDatabase.getReference("cards")
        val lastInt = MutableLiveData<Int>()
        val ref = db.push().key
        db.get().addOnSuccessListener {
            var lastIndex = 0

            for (child in it.children) {
                lastIndex = child.child("cardID").value.toString().toInt()
            }
            lastInt.value = lastIndex
        }
        return lastInt
    }

    fun addCard(model: AddCardModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = firebaseDatabase.getReference("cards")
        db.child(model.cardID.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
            getMaxId()
        }
        return isSuccess
    }
    fun setChildImage(filePath: Uri, imageID: String): MutableLiveData<String> {
        val isSuccess = MutableLiveData<String>()
        if (!filePath.toString().equals("default")) {
            val storageReference = firebaseStorage.getReference("Cards")
            val ref = storageReference.child("uploads/" + UUID.randomUUID().toString())
            val db = firebaseFirestore
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
}