package com.sezer.kirpitci.collection.ui.features.admin.viewcard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentAdminViewCardBinding
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel
import com.sezer.kirpitci.collection.utis.AdminAddNewCardViewmodelFactory
import com.sezer.kirpitci.collection.utis.AdminViewCardAdapter
import com.sezer.kirpitci.collection.utis.AdminViewCardViewModelFactory
import com.sezer.kirpitci.collection.utis.ViewModelFactory


class AdminViewCardFragment : Fragment() {
    private lateinit var binding:FragmentAdminViewCardBinding
    private lateinit var gridLayoutManager:AdminViewCardAdapter
    private lateinit var VM:AdminViewCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAdminViewCardBinding.inflate(inflater,container,false)

        Log.d("TAG", "start: ------------4")
        return binding.root
    }

    override fun onStart() {

        getData()

        Log.d("TAG", "start: ------------1")
        super.onStart()

    }

    override fun onResume() {
        Log.d("TAG", "start: ------------5")
        super.onResume()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialVM()
        Log.d("TAG", "start: ------------2")
        initialRecyler()
        Log.d("TAG", "start: ------------3")
        super.onViewCreated(view, savedInstanceState)
    }
    fun initialRecyler(){
        val gridLayoutItems = ArrayList<ViewCardModel>()
        gridLayoutManager = AdminViewCardAdapter()
        binding.cardRecycler.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = gridLayoutManager
        }


    }
    private fun initialVM() {
        val factory = AdminViewCardViewModelFactory()
        VM = ViewModelProvider(this, factory)[AdminViewCardViewModel::class.java]

    }
    fun getData(){
        VM.getCards().observe(viewLifecycleOwner, Observer {
            gridLayoutManager.submitList(it)
            Log.d("TAG", "getData: "+it.size)

        })

    }

}