package com.sezer.kirpitci.collection.utis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel
import com.sezer.kirpitci.collection.ui.features.splash.SplashViewModel

class SplashViewModelFactory () :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel() as T
    }
}