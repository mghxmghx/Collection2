package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings

data class SendRequestModel(
    val cardName: String,
    var cardInfo: String? = null,
    var cardPrice: String? = null,
    var cardCategory: String? = null,
    var cardCountry: String? = null,
    var cardCity: String? = null
)