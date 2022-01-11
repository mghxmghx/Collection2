package com.sezer.kirpitci.collection.ui.features.user.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithBitmap
import com.sezer.kirpitci.collection.utis.updateWithUrl
import javax.inject.Inject

class UserFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var VM: UserViewModel
    private var categoryTemp = ""
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
        initialRecyler()
        initialTablayout()
        getID("beer")
        initialSearch()
        categoryTemp = "beer"
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
    private lateinit var id: String
    private fun getID(category: String) {
        VM.getUserID().observe(viewLifecycleOwner, Observer {
            id = it
            getData(category, it)
        })
    }
    private fun getData(category: String, id: String){
        VM.getCards(category, id).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            array.clear()
            array.addAll(it)
        })
    }
    private fun initialSearch(){
        binding.searchAlcoholText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length>=3){
                    searchData(alcoholName = p0.toString())
                }
                else if(p0.toString().length == 0){
                    getID(categoryTemp)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
    private var array = arrayListOf<CardModel>()
    private fun searchData(alcoholName: String){
        VM.searchCards(alcoholName, categoryTemp).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })
    }
    private fun mergeData(model:CardModel, status: String){
        initialRecyler()
        val index = array.indexOf(model)
        model.status = status
        array.set(index,model)
        adapter.submitList(array)
    }
    private fun isCheckVM(checked: Boolean, model: CardModel) {
        VM.setCheck(checked, model, id)
        mergeData(model, checked.toString())
    }
    override fun clicked(model: CardModel) {
       checkClickedLayout(model)
    }
    private fun checkClickedLayout(model: CardModel) {
        val view = layoutInflater.inflate(R.layout.detail_dialog_content, null)

        val dialog = context?.let { it1 ->
                BottomSheetDialog(
                    it1,
                    R.style.BottomSheetDialogTheme
                )
            }
            val closeButton = view.findViewById<ImageView>(R.id.dialogContentClose)
        val image = view.findViewById<ImageView>(R.id.dialogImagView)
        val isCheck = view.findViewById<Switch>(R.id.isCheckForAlcohol)
        isCheck.isChecked = model.status.toBoolean()
        isCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isCheck.isChecked()) {
                isCheckVM(isCheck.isChecked(), model)
            } else {
                isCheckVM(isCheck.isChecked(), model)
            }
        })
        image.updateWithUrl(model.cardPath, image, true.toString())
            closeButton.setOnClickListener {
                if (dialog != null) {
                    dialog.cancel()
                    (view.getParent() as ViewGroup).removeView(view)
                }
            }

            if (dialog != null) {
                dialog.setContentView(view)
            }
            if (dialog != null) {
                dialog.show()
            }
        }


    private fun initialTablayout() {
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.searchAlcoholText.text?.clear()
                    categoryTemp = "beer"
                    getID("beer")
                } else if (tab.position == 1) {
                    binding.searchAlcoholText.text?.clear()
                    categoryTemp = "wine"
                    getID("wine")
                } else if (tab.position == 2) {
                    binding.searchAlcoholText.text?.clear()
                    categoryTemp = "cocktail"
                    getID("cocktail")

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}