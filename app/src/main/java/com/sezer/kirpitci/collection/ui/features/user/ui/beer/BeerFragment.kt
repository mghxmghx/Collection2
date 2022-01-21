package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentBeerBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.DetailRecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus
import javax.inject.Inject


class BeerFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentBeerBinding
    private lateinit var VM: BeerFragmentViewModel
    private lateinit var adapter: DetailRecyclerAdapter
    private var categoryTemp: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTablayout()
        initialRecyler()
        focusListener()
        getID("beer")
        categoryTemp = "beer"
        initialSearch()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[BeerFragmentViewModel::class.java]
    }

    private fun initialSearch() {
        binding.searchAlcoholText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length >= 3) {
                    searchData(alcoholName = p0.toString())
                } else if (p0.toString().length == 0) {
                    getID(categoryTemp)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private lateinit var id: String
    private fun getID(category: String) {
        VM.getUserID().observe(viewLifecycleOwner, Observer {
            id = it
            getData(category, it)
        })
    }

    private fun searchData(alcoholName: String) {
        VM.searchCards(alcoholName, categoryTemp, id).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(category: String, s: String) {
        VM.getCards(category, s).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            //  adapter.notifyDataSetChanged()
        })
    }

    fun initialRecyler() {
        adapter = DetailRecyclerAdapter(this)
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 2)
        binding.userCardsRecycler.adapter = adapter
    }

    override fun clicked(model: CardModel) {
        checkClickedLayout(model)
    }

    private fun isCheckVM(checked: Boolean, model: CardModel) {
        VM.setCheck(checked, model, id).observe(viewLifecycleOwner, Observer {
            if (it) {
                //   initialRecyler()
                //   getData(categoryTemp,id)
            }
        })
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun focusListener() {
        binding.searchAlcoholText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
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
        val name = view.findViewById<TextView>(R.id.alcoholName)
        val country = view.findViewById<TextView>(R.id.alcoholCountry)
        val city = view.findViewById<TextView>(R.id.alcoholCity)
        val info = view.findViewById<TextView>(R.id.alcoholInfo)
        val price = view.findViewById<TextView>(R.id.alcoholPrice)
        name.text = model.cardName
        country.text = model.cardCounty
        city.text = model.cardCity
        info.text = model.cardInfo
        price.text = model.cardPrice
        val isCheck = view.findViewById<Switch>(R.id.isCheckForAlcohol)
        isCheck.isChecked = model.status.toBoolean()
        isCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isCheck.isChecked) {
                isCheckVM(isCheck.isChecked, model)
            } else {
                isCheckVM(isCheck.isChecked, model)
            }
        })
        image.updateWithUrlWithStatus(model.cardPath, image, true.toString())
        closeButton.setOnClickListener {
            if (dialog != null) {
                dialog.cancel()
                (view.parent as ViewGroup).removeView(view)
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