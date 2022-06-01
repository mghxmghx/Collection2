package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.drunkbeer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sezer.kirpitci.collection.databinding.FragmentDrunkBeerBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.DrunkBeerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class DrunkBeerFragment : Fragment() {
    private lateinit var binding: FragmentDrunkBeerBinding
    private lateinit var VM: DrunkBeerViewModel
    private lateinit var adapter: DrunkBeerAdapter
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
    companion object {
        const val BEER_CATEGORY = "beer"
        const val WINE_CATEGORY = "wine"
        const val COCKTAIL_CATEGORY = "cocktail"

        const val RUS_COUNTRY = "RUS"
        const val EU_COUNTRY = "EU"
        const val USA_COUNTRY = "USA"

        const val CALC_MINUS = "minus"
        const val CALC_PLUS = "plus"

        const val FALSE = "false"
        const val TRUE = "true"

        const val DEFAULT = "default"
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
        getID()
    }
    private fun initialRecyler() {
        adapter = DrunkBeerAdapter()
        binding.drunkedBeerRV.layoutManager = LinearLayoutManager(context)
        binding.drunkedBeerRV.adapter = adapter
    }
    private lateinit var id: String

    private fun getID(/*category: String*/) {
            VM.getUserID().observe(viewLifecycleOwner, Observer {
                id = it
                Log.d("TAG", "getID: " + it)
                getData(it)
            })
        }

    private fun getData(/*category: String,*/ id: String) {
        VM.getCards("beer", id).observe(viewLifecycleOwner, Observer {
            initialRecyler()
            Log.d("TAG", "getData: " + it)
            val list = arrayListOf<CardModel>()
            for (i in it.indices) {
              if(it.get(i).status == "true")  {
                  list.add(it.get(i))
              }
        }
            adapter.submitList(list)
        })
    }
}