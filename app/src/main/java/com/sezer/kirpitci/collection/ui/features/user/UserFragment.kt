package com.sezer.kirpitci.collection.ui.features.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardModel
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.ViewCardStatusModel
import com.sezer.kirpitci.collection.utis.ClickListener
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.UserAdapter
import com.sezer.kirpitci.collection.utis.factories.UserViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithUrl

class UserFragment : Fragment(), ClickItemUser {
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
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 8)
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



    override fun clicked(model: ViewCardStatusModel) {
        var builder = context?.let { AlertDialog.Builder(it) }
        val v: View? = activity?.layoutInflater?.inflate(R.layout.detail_dialog_content, null)
        val image: ImageView = v?.findViewById(R.id.dialogImagView)!!
        image.updateWithUrl(model.cardPath, image)
        builder?.setView(v)

        builder?.show()
    }

}