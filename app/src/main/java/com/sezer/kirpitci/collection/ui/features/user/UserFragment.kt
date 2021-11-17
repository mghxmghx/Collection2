package com.sezer.kirpitci.collection.ui.features.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.utis.ClickListener
import com.sezer.kirpitci.collection.utis.adapters.UserAdapter
import com.sezer.kirpitci.collection.utis.factories.UserViewModelFactory

class UserFragment : Fragment(), ClickListener {
    private lateinit var binding:FragmentUserBinding
    private lateinit var adapter: UserAdapter
    private lateinit var VM: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentUserBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialVM()
        initialRecyler()
        getData()
        super.onViewCreated(view, savedInstanceState)
    }
    fun initialRecyler(){
        //adapter = AdminViewCardAdapter(this)
        adapter = UserAdapter(mutableListOf(),this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 4)
        binding.userCardsRecycler.adapter = adapter
    }
    private fun initialVM() {
        val factory = UserViewModelFactory()
        VM = ViewModelProvider(this, factory)[UserViewModel::class.java]

    }
    private fun getData(){
        VM.getMyCards().observe(viewLifecycleOwner, Observer {
            VM.getCardInformation(it).observe(viewLifecycleOwner, Observer {
                adapter.swap(it)
            })
        })
    }

    override fun itemUpdateClick(data: ViewCardModel) {
    }

    override fun itemDeleteClick(data: ViewCardModel) {
    }

}