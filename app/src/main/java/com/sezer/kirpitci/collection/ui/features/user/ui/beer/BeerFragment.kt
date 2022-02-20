package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.view_search.*
import kotlinx.android.synthetic.main.view_search.view.*


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
        (activity as AppCompatActivity).supportActionBar?.hide()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[BeerFragmentViewModel::class.java]
    }

    private fun initialSearch() {
        binding.searchBar.search_input_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.e("afterTextChanged",search_input_text.text.toString())
                if(s.toString().length>=3){
                    searchData(s.toString())
                } else if(s.toString().length == 0) {
                    getData(categoryTemp, s.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("onTextChanged",search_input_text.text.toString())
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
        binding.userCardsRecycler.layoutManager = GridLayoutManager(context, 3)
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
      /*  binding.searchAlcoholText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }*/
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
        val voteTotal = view.findViewById<TextView>(R.id.voteTotal)
        name.text = model.cardName
        country.text = model.cardCounty
        city.text = model.cardCity
        info.text = model.cardInfo
        price.text = model.cardPrice
        voteTotal.text = "(" + model.voteCount.toString() + ")"
        val isCheck = view.findViewById<Switch>(R.id.isCheckForAlcohol)
        setBackgrounds(model, view)
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

    private fun setBackgrounds(model: CardModel, view: View) {
        var averageRate = 0
        if (!model.cardAverage.toString().equals("0") && !model.voteCount.toString().equals("0")) {
            averageRate =
                (model.cardAverage.toString().toFloat().toInt() / model.voteCount.toString()
                    .toInt())
        }
        Log.d("TAG", "setBackgrounds: " + averageRate)
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
        for (i in 0 until averageRate) {
            list.get(i).setImageResource(R.drawable.ic_dialog_rate_star_check)
        }
        for (t in averageRate..9) {
            list.get(t).setImageResource(R.drawable.ic_dialog_noncheck_star)
        }
    }

    private fun initialTablayout() {
        binding.ltbeer.isSelected = true
        categoryTemp = "beer"
        binding.ltbeer.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = true
            categoryTemp = "beer"
            getID("beer")
            //
        }
        binding.ltwine.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = true
            binding.ltbeer.isSelected = false
            categoryTemp = "wine"
            getID("wine")

            //
        }
        binding.ltcocktail.setOnClickListener {
            binding.ltcocktail.isSelected = true
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = false
            categoryTemp = "cocktail"
            getID("cocktail")

            //
        }

    }


}