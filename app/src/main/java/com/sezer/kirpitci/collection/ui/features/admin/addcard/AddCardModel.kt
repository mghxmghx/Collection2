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
    var cardABV: String,
    var cardType: String,
    var cardCompany: String,
    var cardAverage: String,
    var voteCount: String,
    var beerInCountry: String,
    var beerML: String? = null
)