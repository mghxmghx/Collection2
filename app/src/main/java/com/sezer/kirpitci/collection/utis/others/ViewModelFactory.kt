package com.sezer.kirpitci.collection.utis.others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>,
            Provider<ViewModel>>
) : ViewModelProvider.Factory {
    private val viewModelMap = HashMap<Class<out ViewModel>, ViewModel>()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (viewModelMap.contains(modelClass)) {
            return viewModelMap[modelClass] as T
        }
        val viewModelProvider =
            viewModels[modelClass] ?: throw IllegalArgumentException(
                " model class $modelClass not found"
            )
        val viewModel = viewModelProvider.get()
        viewModelMap[viewModel::class.java] = viewModel
        return viewModel as T
    }
}
