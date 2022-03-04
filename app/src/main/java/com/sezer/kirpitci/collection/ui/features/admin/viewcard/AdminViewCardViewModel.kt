package com.sezer.kirpitci.collection.ui.features.admin.viewcard

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

class AdminViewCardViewModel @Inject constructor(val firebaseDatabase: FirebaseDatabase) :
    ViewModel() {
    companion object {
        const val CARD_ID = "cardID"
        const val CARD_NAME = "cardName"
        const val CARD_INFO = "cardInfo"
        const val CARD_CATEGORY = "cardCategory"
        const val CARD_COUNTRY = "cardCounty"
        const val CARD_CITY = "cardCity"
        const val CARD_PRICE = "cardPrice"
        const val CARD_PATH = "cardPath"
        const val CARDS = "cards"
        const val DEFAULT = "default"
        const val CARDSV2 = "Cards"
    }

    var quote: MutableLiveData<List<ViewCardModel>>? = getCards()
    fun getCards(): MutableLiveData<List<ViewCardModel>> {
        val list = MutableLiveData<List<ViewCardModel>>()
        val cardList = ArrayList<ViewCardModel>()
        val db2 = firebaseDatabase.getReference(CARDS)
        db2.get()
            .addOnSuccessListener {
                for (child in it.children) {
                    cardList.add(
                        ViewCardModel(
                            child.child(CARD_ID).value.toString(),
                            child.child(CARD_NAME).value.toString(),
                            child.child(CARD_INFO).value.toString(),
                            child.child(CARD_CATEGORY).value.toString(),
                            child.child(CARD_COUNTRY).value.toString(),
                            child.child(CARD_CITY).value.toString(),
                            child.child(CARD_PRICE).value.toString(),
                            child.child(CARD_PATH).value.toString(),
                        )
                    )
                }
                list.value = cardList
            }
            .addOnFailureListener {}
        return list
    }

    fun deleteChildren(model: ViewCardModel) {
        val reqestDB = firebaseDatabase.getReference(CARDS)
        reqestDB.child(model.cardID).removeValue()
    }

    fun setChildImage(filePath: Uri, imageID: String): MutableLiveData<String> {
        val isSuccess = MutableLiveData<String>()
        if (!filePath.toString().equals(DEFAULT)) {
            val storageReference = FirebaseStorage.getInstance().getReference(CARDSV2)
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

    fun updateCard(newModel: ViewCardModel): MutableLiveData<String> {
        val reqestDB = firebaseDatabase.getReference(CARDS)
        val isSuccess = MutableLiveData<String>()
        reqestDB.child(newModel.cardID).child(CARD_CATEGORY).setValue(newModel.cardCategory)
        reqestDB.child(newModel.cardID).child(CARD_CITY).setValue(newModel.cardCity)
        reqestDB.child(newModel.cardID).child(CARD_COUNTRY).setValue(newModel.cardCounty)
        reqestDB.child(newModel.cardID).child(CARD_NAME).setValue(newModel.cardName)
        reqestDB.child(newModel.cardID).child(CARD_INFO).setValue(newModel.cardInfo)
        reqestDB.child(newModel.cardID).child(CARD_PATH).setValue(newModel.cardPath)
        reqestDB.child(newModel.cardID).child(CARD_PRICE).setValue(newModel.cardPrice)
            .addOnCompleteListener {
                isSuccess.value = it.isSuccessful.toString()
            }
        return isSuccess
    }

}