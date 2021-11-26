package com.sezer.kirpitci.collection.ui.features.admin.viewusers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sezer.kirpitci.collection.databinding.FragmentViewUsersBinding
import com.sezer.kirpitci.collection.utis.adapters.AdapterUserView
import com.sezer.kirpitci.collection.utis.factories.ViewUsersViewModelFactory


class ViewUsersFragment : Fragment() {
    private lateinit var binding: FragmentViewUsersBinding
    private lateinit var VM: ViewUsersViewModel
    private lateinit var adapter: AdapterUserView

    private fun initialRcy(){
        adapter= AdapterUserView(mutableListOf())
        binding.ViewUserRcy.layoutManager=LinearLayoutManager(context)
        binding.ViewUserRcy.adapter=adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentViewUsersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory= ViewUsersViewModelFactory()
        VM= ViewModelProvider(this,factory).get(ViewUsersViewModel::class.java)
        initialRcy()
        getUsers()

        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }
    fun getUsers(){
        VM.getUsers().observe(viewLifecycleOwner, Observer {
          adapter.swap(it)

        })

    }
}