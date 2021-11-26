package com.sezer.kirpitci.collection.utis.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.admin.viewusers.ViewUsersViewModel
import com.sezer.kirpitci.collection.ui.features.user.UserViewModel

class ViewUsersViewModelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewUsersViewModel() as T
    }
}