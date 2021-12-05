package com.sezer.kirpitci.collection.ui.features.registration

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.PersonalSettingsViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RegistirationModule {
    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: RegistrationViewModel): ViewModel
}