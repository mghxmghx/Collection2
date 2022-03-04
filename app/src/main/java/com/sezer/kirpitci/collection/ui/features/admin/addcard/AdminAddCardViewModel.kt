package com.sezer.kirpitci.collection.ui.features.admin.addcard

import android.net.Uri
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


class AdminAddCardViewModel @Inject constructor(
    val firebaseDatabase: FirebaseDatabase,
    val firebaseStorage: FirebaseStorage,
    val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    companion object {
        const val CARDS = "cards"
        const val CARDSV2 = "Cards"
        const val CARD_ID = "cardID"
        const val DEFAULT = "default"
        const val UPLOADS = "uploads/"
        const val COMPANIES = "companies"
        const val CATEGORIES = "categories"
        const val USERS = "users"
        const val EMAIL = "email"
    }

    fun getMaxId(): MutableLiveData<Int> {
        val db = firebaseDatabase.getReference(CARDS)
        val lastInt = MutableLiveData<Int>()

        db.get().addOnSuccessListener {
            var lastIndex = 0

            for (child in it.children) {
                lastIndex = child.child(CARD_ID).value.toString().toInt()
            }
            lastInt.value = lastIndex
        }
        return lastInt
    }

    fun addCard(model: AddCardModel): MutableLiveData<Boolean> {
        val isSuccess = MutableLiveData<Boolean>()
        val db = firebaseDatabase.getReference(CARDS)
        db.child(model.cardID.toString()).setValue(model).addOnCompleteListener {
            isSuccess.value = it.isSuccessful
            getMaxId()
        }
        return isSuccess
    }

    fun setChildImage(filePath: Uri, imageID: String): MutableLiveData<String> {
        val isSuccess = MutableLiveData<String>()
        if (!filePath.toString().equals(DEFAULT)) {
            val storageReference = firebaseStorage.getReference(CARDSV2)
            val ref = storageReference.child(UPLOADS + UUID.randomUUID().toString())
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
                            isSuccess.value = DEFAULT
                        }
                } else {
                    isSuccess.value = DEFAULT
                }
            }
        } else {
            isSuccess.value = DEFAULT
        }
        return isSuccess
    }

    fun getCompanyList(): MutableLiveData<List<String>> {
        val list = arrayListOf<String>()
        val returnList = MutableLiveData<List<String>>()
        firebaseDatabase.getReference(COMPANIES).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(child.value.toString())
            }
            returnList.value = list
        }
        return returnList
    }

    fun getCountryList(): MutableLiveData<List<String>> {
        val list = arrayListOf<String>()
        val returnList = MutableLiveData<List<String>>()
        firebaseDatabase.getReference(CATEGORIES).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(child.value.toString())
            }
            returnList.value = list
        }
        return returnList
    }

    fun getCategoryList(): MutableLiveData<List<String>> {
        val list = arrayListOf<String>()
        val returnList = MutableLiveData<List<String>>()
        firebaseDatabase.getReference(CATEGORIES).get().addOnSuccessListener {
            for (child in it.children) {
                list.add(child.value.toString())
            }
            returnList.value = list
        }
        return returnList
    }

    fun checkUserList(): MutableLiveData<List<AddCardUserModel>> {
        val list = MutableLiveData<List<AddCardUserModel>>()
        val tempList = ArrayList<AddCardUserModel>()
        firebaseDatabase.getReference(USERS).get().addOnSuccessListener {
            for (child in it.children) {
                tempList.add(
                    AddCardUserModel(
                        child.key.toString(),
                        child.child(EMAIL).value.toString(),
                        false.toString()
                    )
                )
            }
            list.value = tempList
        }
        return list
    }

    fun addUserUnderCard(list: List<AddCardUserModel>, cardID: String): MutableLiveData<Boolean> {
        val isSucces = MutableLiveData<Boolean>()
        firebaseDatabase.getReference(CARDS).child(cardID).child(USERS).setValue(list)
            .addOnCompleteListener {
                isSucces.value = it.isSuccessful
            }
        return isSucces
    }
}