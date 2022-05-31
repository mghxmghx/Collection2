package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentDrunkBeerBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo.PersonalInfoViewModel
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class DrunkBeerFragment : Fragment() {
    private lateinit var binding: FragmentDrunkBeerBinding
    private lateinit var VM: DrunkBeerViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentDrunkBeerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[DrunkBeerViewModel::class.java]
    }
}