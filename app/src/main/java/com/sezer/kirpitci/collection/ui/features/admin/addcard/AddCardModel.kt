package com.sezer.kirpitci.collection.ui.features.admin.addcard

data class AddCardModel(
    var cardID: String,
    var cardName: String,
    var cardInfo: String?,
    var cardCategory: String,
    var cardCounty: String,
    var cardCity: String?,
    var cardPrice: String?,
    var cardPath: String,
    var cardAverage: String,
    var voteCount: String
)