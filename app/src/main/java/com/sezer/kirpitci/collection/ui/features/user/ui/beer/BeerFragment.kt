package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentBeerBinding
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.DetailRecyclerAdapter
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.factories.DetailViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap

class BeerFragment : Fragment(), ClickItemUser {
    private lateinit var binding: FragmentBeerBinding
    private lateinit var VM: BeerFragmentViewModel
    private lateinit var adapter: RecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialVM()
        getData()
        initialTablayout()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialVM() {
        val factory = DetailViewModelFactory()
        VM = ViewModelProvider(this, factory)[BeerFragmentViewModel::class.java]

    }

    private fun getData() {
        VM.getMyCards().observe(viewLifecycleOwner, Observer {
            arrayList.addAll(it)
            separateData("beer")
        })
    }
    val arrayList = arrayListOf<CardModel>()
    val list = arrayListOf<CardModel>()
    private fun separateData(category: String){
        list.clear()
        if(arrayList.size !=0){
            for(i in 0..arrayList.size-1)
            {
                if(category.equals(arrayList.get(i).cardCategory)){
                    list.add(arrayList.get(i))
                }
            }
        }
        initialRecyler()
        adapter.submitList(list)
    }

    fun initialRecyler() {
        adapter = RecyclerAdapter(this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.userCardsRecycler.adapter = adapter
    }

    override fun clicked(model: CardModel) {
        var builder = context?.let { AlertDialog.Builder(it) }
        val v: View? = activity?.layoutInflater?.inflate(R.layout.detail_dialog_content, null)
        val image: ImageView = v?.findViewById(R.id.dialogImagView)!!
        image.updateWithBitmap(model.cardPath)
        builder?.setView(v)
        builder?.show()
    }

    private fun initialTablayout() {
        separateData("beer")
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    separateData("beer")
                } else if (tab.position == 1) {
                    separateData("wine")

                } else if (tab.position == 2) {
                    separateData("cocktail")

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }


}