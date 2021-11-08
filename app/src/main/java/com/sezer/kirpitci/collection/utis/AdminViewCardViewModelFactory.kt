package com.sezer.kirpitci.collection.utis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.AdminViewCardViewModel
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel

class AdminViewCardViewModelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AdminViewCardViewModel() as T
    }
}