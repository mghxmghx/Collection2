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
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.UserAdapter
import com.sezer.kirpitci.collection.utis.factories.UserViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap

class UserFragment : Fragment(), ClickItemUser {
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserAdapter
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
        initialVM()
        initialRecyler()
        initialTablayout()
        binding.customProgress1.max = 10
        binding.customProgress2.max = 10
        val currentProgress = 6
        val currentProgress2 = 8
        ObjectAnimator.ofInt(binding.customProgress1, "progress", currentProgress)
            .setDuration(2000)
            .start()
        super.onViewCreated(view, savedInstanceState)
        ObjectAnimator.ofInt(binding.customProgress2, "progress", currentProgress2)
            .setDuration(2000)
            .start()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initialRecyler() {
        //adapter = AdminViewCardAdapter(this)
        adapter = UserAdapter(mutableListOf(), this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 8)
        binding.userCardsRecycler.adapter = adapter
    }

    private fun initialVM() {
        val factory = UserViewModelFactory()
        VM = ViewModelProvider(this, factory)[UserViewModel::class.java]

    }

    private fun getData(category: String) {
        Log.d("TAG", "getData: " + category)
        VM.getMyCards().observe(viewLifecycleOwner, Observer {
            VM.getCardInformation(it, category).observe(viewLifecycleOwner, Observer {
                Log.d("TAG", "getData: " + it.size)
                adapter.swap(it)
            })
        })
    }


    override fun clicked(model: ViewCardStatusModel) {
        var builder = context?.let { AlertDialog.Builder(it) }
        val v: View? = activity?.layoutInflater?.inflate(R.layout.detail_dialog_content, null)
        val image: ImageView = v?.findViewById(R.id.dialogImagView)!!
        image.updateWithBitmap(model.cardPath)
        builder?.setView(v)

        builder?.show()
    }

    private fun initialTablayout() {
        Log.d("TAG", "initialTablayout: " + "beer")
        getData("beer")
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    Log.d("TAG", "onTabSelected: ")
                    getData("beer")
                } else if (tab.position == 1) {
                    getData("wine")

                } else if (tab.position == 2) {
                    getData("cocktail")

                } else {
                    Log.d("TAG", "onTabSelected1: ")

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}