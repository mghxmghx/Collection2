package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentSettingsBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var VM: PersonalSettingsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        clickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[PersonalSettingsViewModel::class.java]

    }
    private fun clickListener(){
        binding.button2Settings.setOnClickListener {
           if(!binding.addCardNameTextSettings.text.toString().isNullOrEmpty()) {
               sendRequest()
           } else {
               Toast.makeText(context, "Please enter alcohol name", Toast.LENGTH_LONG).show()
           }
        }
    }
    private fun sendRequest() {
        setClickable(false)
        val model = SendRequestModel(cardName = binding.addCardNameTextSettings.text.toString())
        if(!binding.cardCategoryTextSettings.text.toString().isNullOrEmpty()){
            model.cardCategory = binding.cardCategoryTextSettings.text.toString()
        }
        if(!binding.cardCityTextSettings.text.toString().isNullOrEmpty()){
            model.cardCity = binding.cardCityTextSettings.text.toString()
        }
        if(!binding.cardCountryTextSettings.text.toString().isNullOrEmpty()){
            model.cardCountry = binding.cardCountryTextSettings.text.toString()
        }
        if(!binding.cardInfoTextSettings.text.toString().isNullOrEmpty()){
            model.cardInfo = binding.cardInfoTextSettings.text.toString()
        }
        if(!binding.cardPriceTextSettings.text.toString().isNullOrEmpty()){
            model.cardPrice = binding.cardPriceTextSettings.text.toString()
        }
        VM.sendRequest(model).observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(context, "Request Added", Toast.LENGTH_LONG).show()
                binding.addCardNameTextSettings.text?.clear()
                binding.cardCategoryTextSettings.text?.clear()
                binding.cardCityTextSettings.text?.clear()
                binding.cardCountryTextSettings.text?.clear()
                binding.cardInfoTextSettings.text?.clear()
                binding.cardPriceTextSettings.text?.clear()
            } else{
                Toast.makeText(context, "Somethings went wrong!", Toast.LENGTH_LONG).show()
            }
            setClickable(true)
        })
    }
    private fun setClickable(isClickable: Boolean){
        binding.button2Settings.isClickable = isClickable
        binding.button2Settings.isEnabled = isClickable
    }

}