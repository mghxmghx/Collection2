package com.sezer.kirpitci.collection.ui.features.admin.viewcard

data class ViewCardModel(var cardID:String,var cardName:String, var cardInfo:String?, var cardCategory:String, var cardCounty:String, var cardCity:String?, var cardPrice:String?, var cardPath:String)
data class ViewCardStatusModel(var cardID:String,var cardName:String, var cardInfo:String?, var cardCategory:String, var cardCounty:String, var cardCity:String?, var cardPrice:String?, var cardPath:String, var status: String)