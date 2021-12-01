package com.sezer.kirpitci.collection.utis.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.user.ui.dashboard.BeerFragmentViewModel

class DetailViewModelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BeerFragmentViewModel() as T
    }
}