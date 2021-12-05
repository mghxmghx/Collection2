package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class PersonalSettingsModule {
    @Binds
    @IntoMap
    @ViewModelKey(PersonalSettingsViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: PersonalSettingsViewModel): ViewModel
}