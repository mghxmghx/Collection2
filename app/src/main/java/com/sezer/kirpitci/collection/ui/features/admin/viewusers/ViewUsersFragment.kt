package com.sezer.kirpitci.collection.ui.features.admin.viewusers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sezer.kirpitci.collection.databinding.FragmentViewUsersBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.utis.adapters.AdapterUserView
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject


class ViewUsersFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentViewUsersBinding
    private lateinit var VM: ViewUsersViewModel
    private lateinit var adapter: AdapterUserView

    private fun initialRcy() {
        adapter = AdapterUserView(mutableListOf())
        binding.ViewUserRcy.layoutManager = GridLayoutManager(context, 8)
        binding.ViewUserRcy.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        VM = ViewModelProvider(this, viewModelFactory).get(ViewUsersViewModel::class.java)
        initialRcy()
        getUsers()

        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialUI(){
        MyApp.appComponent.inject(this)
    }
    fun getUsers() {
        VM.getUsers().observe(viewLifecycleOwner, Observer {
            adapter.swap(it)

        })

    }
}