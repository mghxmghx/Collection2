package com.sezer.kirpitci.collection.ui.features.user.home

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus
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
        hideKeyboardx()
        getID("beer")
        initialSearch()
        categoryTemp = "beer"
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setStatus(totalCount: Int, totalTrueStatus: Int) {
     //   binding.customProgress1.max = 10
        binding.customProgress2.max = totalCount
    //    val currentProgress = 6
        val currentProgress2 = totalTrueStatus
        binding.progress2NumberText.text = "$totalTrueStatus/$totalCount"
     /*   ObjectAnimator.ofInt(binding.customProgress1, "progress", currentProgress)
            .setDuration(1000)
            .start() */
        ObjectAnimator.ofInt(binding.customProgress2, "progress", currentProgress2)
            .setDuration(1000)
            .start()
    }
    private fun countAlcoholStatus(list: List<CardModel>?, isCheckted: String) {
        var totalCount = 0
        var totalTrueStatus = 0
        if(isCheckted.equals("plus")){
            val text = binding.progress2NumberText.text.split("/")
            totalCount = text.get(1).toInt()
            totalTrueStatus = text.get(0).toInt()+1
        } else if(isCheckted.equals("minus")){
            val text = binding.progress2NumberText.text.split("/")
            totalCount = text.get(1).toInt()
            totalTrueStatus = text.get(0).toInt()-1
        }else{
            if (list != null) {
                for(i in 0 until list.size){
                    totalCount++
                    if(list.get(i).status.equals("true")){
                        totalTrueStatus++
                    }
                }
            }
        }
        setStatus(totalCount, totalTrueStatus)
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

    fun hideKeyboardx(){

        binding.searchAlcoholText.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            Log.d("TAG", "hideKeyboardx: " + hasFocus)

            if (!hasFocus) {
                Log.d("TAG", "hideKeyboardx: "+ hasFocus)
               // Utils.hideSoftKeyboard(activity)
                hideSoftKeyboard(v)
            }
        })
    }
    fun hideSoftKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private fun getData(category: String, id: String){
        VM.getCards(category, id).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        //    array.clear()
        //    array.addAll(it)
            countAlcoholStatus(it, "default")
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
    private fun searchData(alcoholName: String){
        VM.searchCards(alcoholName, categoryTemp, id).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun isCheckVM(checked: Boolean, model: CardModel) {
        VM.setCheck(checked, model, id).observe(viewLifecycleOwner, Observer {
            initialRecyler()
            getData(categoryTemp,id)
        })
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
        val startOne = view.findViewById<ImageView>(R.id.dialog_star_one)
        val startTwo = view.findViewById<ImageView>(R.id.dialog_star_two)
        val startThree = view.findViewById<ImageView>(R.id.dialog_star_three)
        val startFour = view.findViewById<ImageView>(R.id.dialog_star_four)
        val startFive = view.findViewById<ImageView>(R.id.dialog_star_five)
        val startSix = view.findViewById<ImageView>(R.id.dialog_star_six)
        val startSeven = view.findViewById<ImageView>(R.id.dialog_star_seven)
        val startEight = view.findViewById<ImageView>(R.id.dialog_star_eight)
        val startNine = view.findViewById<ImageView>(R.id.dialog_star_nine)
        val startTen = view.findViewById<ImageView>(R.id.dialog_star_ten)
        startOne.setOnClickListener {
            starControl(1, view, model)
        }
        startTwo.setOnClickListener {
            starControl(2, view, model)
        }
        startThree.setOnClickListener {
            starControl(3, view, model)
        }
        startFour.setOnClickListener {
            starControl(4, view, model)
        }
        startFive.setOnClickListener {
            starControl(5, view, model)
        }
        startSix.setOnClickListener {
            starControl(6, view, model)
        }
        startSeven.setOnClickListener {
            starControl(7, view, model)
        }
        startEight.setOnClickListener {
            starControl(8, view, model)
        }
        startNine.setOnClickListener {
            starControl(9, view, model)
        }
        startTen.setOnClickListener {
            starControl(10,view, model)
        }
        val image = view.findViewById<ImageView>(R.id.dialogImagView)
        val nameTw = view.findViewById<TextView>(R.id.alcoholName)
        val countryTw = view.findViewById<TextView>(R.id.alcoholCountry)
        val priceTw = view.findViewById<TextView>(R.id.alcoholPrice)
        val cityTw = view.findViewById<TextView>(R.id.alcoholCity)
        val infoTw = view.findViewById<TextView>(R.id.alcoholInfo)
        nameTw.text = model.cardName
        countryTw.text = model.cardCounty
        priceTw.text = model.cardPrice
        cityTw.text = model.cardCity
        infoTw.text = model.cardInfo
        if(!model.userStarRate.isNullOrEmpty() && !model.userStarRate.toString().equals("null")){
            setBackGrounds(model.userStarRate!!.toInt(), view, model)
        } else {
            setBackGrounds(0, view, model)

        }
        val isCheck = view.findViewById<Switch>(R.id.isCheckForAlcohol)
        isCheck.isChecked = model.status.toBoolean()
        isCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isCheck.isChecked()) {
                isCheckVM(true, model)
                countAlcoholStatus(null, "plus")
            } else {
                isCheckVM(false, model)
                countAlcoholStatus(null, "minus")

            }
        })

        image.updateWithUrlWithStatus(model.cardPath, image, true.toString())
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
    private fun starControl(i: Int, view: View, model: CardModel) {
        setBackGrounds(i,view,model)
        setStarInFB(model)
    }
    private fun setBackGrounds(clickedNumber: Int, view: View, model: CardModel){
        val list = ArrayList<ImageView>()
        list.add(view.findViewById(R.id.dialog_star_one))
        list.add(view.findViewById(R.id.dialog_star_two))
        list.add(view.findViewById(R.id.dialog_star_three))
        list.add(view.findViewById(R.id.dialog_star_four))
        list.add(view.findViewById(R.id.dialog_star_five))
        list.add(view.findViewById(R.id.dialog_star_six))
        list.add(view.findViewById(R.id.dialog_star_seven))
        list.add(view.findViewById(R.id.dialog_star_eight))
        list.add(view.findViewById(R.id.dialog_star_nine))
        list.add(view.findViewById(R.id.dialog_star_ten))
        for(i in 0 until clickedNumber){
            list.get(i).setImageResource(R.drawable.ic_dialog_rate_star_check)
        }
        for(t in clickedNumber..9 ){
            list.get(t).setImageResource(R.drawable.ic_dialog_noncheck_star)
        }
        model.userStarRate = clickedNumber.toString()

    }
    private fun setStarInFB(model: CardModel){
        VM.setStarInFB(model, id)
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
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}