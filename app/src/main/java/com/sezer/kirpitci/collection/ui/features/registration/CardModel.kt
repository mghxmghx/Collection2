package com.sezer.kirpitci.collection.ui.features.registration

data class CardModel(
    var cardID: String,
    var cardName: String,
    var cardInfo: String?,
    var cardCategory: String,
    var cardCounty: String,
    var cardCity: String?,
    var cardPrice: String?,
    var cardPath: String,
    var cardStarAverage: String?,
    var status: String,
    var userStarRate: String?
)