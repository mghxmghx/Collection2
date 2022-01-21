package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PersonalInfoModule {
    @Binds
    @IntoMap
    @ViewModelKey(PersonalInfoViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: PersonalInfoViewModel): ViewModel
}