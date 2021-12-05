package com.sezer.kirpitci.collection.di

import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
