package com.sezer.kirpitci.collection.ui.features.admin.addcard

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.PersonalSettingsViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AdminAddCardModule {
    @Binds
    @IntoMap
    @ViewModelKey(AdminAddCardViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: AdminAddCardViewModel): ViewModel
}