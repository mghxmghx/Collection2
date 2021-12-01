package com.sezer.kirpitci.collection.ui.features.user.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentBeerBinding
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.DetailRecyclerAdapter
import com.sezer.kirpitci.collection.utis.factories.DetailViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap

class BeerFragment : Fragment(), ClickItemUser {
    private lateinit var binding: FragmentBeerBinding
    private lateinit var VM: BeerFragmentViewModel
    private lateinit var adapter: DetailRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialVM()
        initialRecyler()
        initialTablayout()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialVM() {
        val factory = DetailViewModelFactory()
        VM = ViewModelProvider(this, factory)[BeerFragmentViewModel::class.java]

    }

    private fun getData(category: String) {
        VM.getMyCards().observe(viewLifecycleOwner, Observer {
            VM.getCardInformation(it,category).observe(viewLifecycleOwner, Observer {
                adapter.swap(it)
            })
        })
    }
    fun initialRecyler() {
        //adapter = AdminViewCardAdapter(this)
        adapter = DetailRecyclerAdapter(mutableListOf(), this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.userCardsRecycler.adapter = adapter
    }

    override fun clicked(model: ViewCardStatusModel) {
        var builder = context?.let { AlertDialog.Builder(it) }
        val v: View? = activity?.layoutInflater?.inflate(R.layout.detail_dialog_content, null)
        val image: ImageView = v?.findViewById(R.id.dialogImagView)!!
        image.updateWithBitmap(model.cardPath)
        builder?.setView(v)

        builder?.show()
    }
    private fun initialTablayout(){
        getData("beer")
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0 ){
                    getData("beer")
                } else if(tab.position == 1){
                    getData("wine")

                } else if(tab.position == 2){
                    getData("cocktail")

                } else{

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }


}