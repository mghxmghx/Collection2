package com.sezer.kirpitci.collection.utis

import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel

interface ClickListener {
    fun itemUpdateClick(data: ViewCardModel)
    fun itemDeleteClick(data: ViewCardModel)
}