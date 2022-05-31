package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DrunkBeerModule {
    @Binds
    @IntoMap
    @ViewModelKey(DrunkBeerViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: DrunkBeerViewModel): ViewModel
}