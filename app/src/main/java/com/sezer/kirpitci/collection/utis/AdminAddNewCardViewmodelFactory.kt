package com.sezer.kirpitci.collection.utis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.admin.addcard.AdminAddCardViewModel
import com.sezer.kirpitci.collection.ui.features.splash.SplashViewModel

class AdminAddNewCardViewmodelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdminAddCardViewModel() as T
    }
}