package com.sezer.kirpitci.collection.ui.features.user.home

data class SendMessageModel(
    var commentUser: String? = "",
    val comment: String,
    val commentTime: String,
    val cardId: String
)