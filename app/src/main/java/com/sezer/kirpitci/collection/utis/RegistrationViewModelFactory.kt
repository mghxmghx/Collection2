package com.sezer.kirpitci.collection.utis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.registration.RegistrationViewModel
import com.sezer.kirpitci.collection.ui.features.splash.SplashViewModel

class RegistrationViewModelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistrationViewModel() as T
    }
}