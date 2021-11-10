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
import com.sezer.kirpitci.collection.utis.*


class AdminViewCardFragment : Fragment(),ClickListener {
    private lateinit var binding:FragmentAdminViewCardBinding
    private lateinit var adapter:AdminViewCardAdapter
    private lateinit var VM:AdminViewCardViewModel
    var adapterX=AdapterX(ArrayList<ViewCardModel>(),this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAdminViewCardBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onStart() {

        getData()


        super.onStart()

    }

    override fun onResume() {

        super.onResume()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialVM()

        initialRecyler()

        super.onViewCreated(view, savedInstanceState)
    }

    fun initialRecyler(){
        //adapter = AdminViewCardAdapter(this)
        binding.cardRecycler.layoutManager = LinearLayoutManager(context)
        binding.cardRecycler.adapter = adapterX



    }
    private fun initialVM() {
        val factory = AdminViewCardViewModelFactory()
        VM = ViewModelProvider(this, factory)[AdminViewCardViewModel::class.java]

    }
    fun getData(){
        VM.getCards().observe(viewLifecycleOwner, Observer {

           // adapter.notifyDataSetChanged()
            //adapter.submitList(it)
            adapterX.swap(it)
            //adapter.notifyDataSetChanged()
            Log.d("TAG", "getData: "+it.size)

        })

    }

    override fun itemAcceptClick(data: ViewCardModel) {
    }

    override fun itemDeleteClick(data: ViewCardModel) {

        VM.deleteChildren(data)
        Log.d("TAG", "getData: "+data.cardID)
        getData()
        Log.d("TAG", "getData: "+data.cardID)
    }

}