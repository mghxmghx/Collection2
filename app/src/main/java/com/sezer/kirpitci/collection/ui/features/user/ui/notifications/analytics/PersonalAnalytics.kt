package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.R

class PersonalAnalytics : Fragment() {

    companion object {
        fun newInstance() = PersonalAnalytics()
    }

    private lateinit var viewModel: PersonalAnalyticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.personal_analytics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalAnalyticsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}