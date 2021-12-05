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
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.DetailRecyclerAdapter
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap
import javax.inject.Inject

class BeerFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentBeerBinding
    private lateinit var VM: BeerFragmentViewModel
    private lateinit var adapter: DetailRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTablayout()
        initialRecyler()
        getData("beer")
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI(){
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[BeerFragmentViewModel::class.java]
    }

    private fun getData(category: String) {
        VM.getCards(category).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    fun initialRecyler() {
        adapter = DetailRecyclerAdapter(this)
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
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    getData("beer")
                } else if (tab.position == 1) {
                    getData("wine")

                } else if (tab.position == 2) {
                    getData("cocktail")

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }


}