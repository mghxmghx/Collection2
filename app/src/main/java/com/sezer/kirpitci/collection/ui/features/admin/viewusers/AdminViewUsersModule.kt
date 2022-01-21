package com.sezer.kirpitci.collection.ui.features.admin.viewusers

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdminViewUsersModule {
    @Binds
    @IntoMap
    @ViewModelKey(ViewUsersViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: ViewUsersViewModel): ViewModel
}