package com.sezer.kirpitci.collection.ui.features.admin.viewcard

data class ViewCardModel(
    var cardID: String,
    var cardName: String,
    var cardInfo: String?,
    var cardCategory: String,
    var cardCounty: String,
    var cardCity: String? = null,
    var cardPrice: String?,
    var cardPath: String,
    var cardABV: String,
    var cardType: String,
    var cardCompany: String,
    var cardAverage: String,
    var voteCount: String,
    var beerInCountry: String,
    var beerML: String? = null,
    var prop1: String? = " ",
    var prop2: String? = " ",
    var prop3: String? = " ",
    var prop4: String? = " ",
    var prop5: String? = " ",
    var prop6: String? = " ",
)

data class ViewCardStatusModel(
    var cardID: String,
    var cardName: String,
    var cardInfo: String?,
    var cardCategory: String,
    var cardCounty: String,
    var cardCity: String?,
    var cardPrice: String?,
    var cardPath: String,
    var status: String
)