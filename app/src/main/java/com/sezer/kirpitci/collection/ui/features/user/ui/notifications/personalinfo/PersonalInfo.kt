package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.R

class PersonalInfo : Fragment() {

    companion object {
        fun newInstance() = PersonalInfo()
    }

    private lateinit var viewModel: PersonalInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.personal_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PersonalInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}