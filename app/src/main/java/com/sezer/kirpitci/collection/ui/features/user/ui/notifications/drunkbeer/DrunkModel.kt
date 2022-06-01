package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer

data class DrunkModel(val childID: String, val beerName: String, var userStarRate: String? = "0", val beerPhoto : String, var comments: List<CommentModel>? = null)