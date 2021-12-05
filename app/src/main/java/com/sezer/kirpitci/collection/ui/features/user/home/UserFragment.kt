package com.sezer.kirpitci.collection.ui.features.user.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap
import javax.inject.Inject

class UserFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var VM: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        getData()
        initialRecyler()
        initialTablayout()
        binding.customProgress1.max = 10
        binding.customProgress2.max = 10
        val currentProgress = 6
        val currentProgress2 = 8
        ObjectAnimator.ofInt(binding.customProgress1, "progress", currentProgress)
            .setDuration(1000)
            .start()
        super.onViewCreated(view, savedInstanceState)
        ObjectAnimator.ofInt(binding.customProgress2, "progress", currentProgress2)
            .setDuration(1000)
            .start()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initialRecyler() {
        adapter = RecyclerAdapter(this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 8)
        binding.userCardsRecycler.adapter = adapter
    }
    private fun initialUI(){
        MyApp.appComponent.inject(this)
    }
    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

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
            Log.d("TAG", "separateData: ")
            for(i in 0..arrayList.size-1)
            {
                if(category.equals(arrayList.get(i).cardCategory)){
                    Log.d("TAG", "separateData: " + arrayList.get(i).cardCategory)
                    list.add(arrayList.get(i))
                }
            }
        }
        initialRecyler()
        adapter.submitList(list)
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