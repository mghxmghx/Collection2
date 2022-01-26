package com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis

import androidx.lifecycle.ViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GeneralAnalysisModule {
    @Binds
    @IntoMap
    @ViewModelKey(GeneralAnalysisViewModel::class)
    internal abstract fun bindGeneralAnalysisViewModel(viewModel: GeneralAnalysisViewModel): ViewModel
}