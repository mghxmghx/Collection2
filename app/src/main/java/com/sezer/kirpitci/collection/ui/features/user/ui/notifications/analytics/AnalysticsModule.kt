package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AnalysticsModule {
    @Binds
    @IntoMap
    @ViewModelKey(PersonalAnalyticsViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: PersonalAnalyticsViewModel): ViewModel
}