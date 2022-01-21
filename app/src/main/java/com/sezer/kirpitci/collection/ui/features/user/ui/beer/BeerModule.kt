package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BeerModule {
    @Binds
    @IntoMap
    @ViewModelKey(BeerFragmentViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: BeerFragmentViewModel): ViewModel
}