package com.sezer.kirpitci.collection.ui.features.user.ui.beer

import SpinnerAdapterr
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentBeerBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.CommentRecyclerAdapter
import com.sezer.kirpitci.collection.utis.adapters.DetailRecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.others.updateWithUrlWithStatus
import kotlinx.android.synthetic.main.view_search.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class BeerFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentBeerBinding
    private lateinit var VM: BeerFragmentViewModel
    private lateinit var adapter: DetailRecyclerAdapter
    private lateinit var commentAdapter: CommentRecyclerAdapter
    private var categoryTemp: String = ""
    private var language = ""
    private var countryPosition = 0
    private var beerTypePosition = 0
    private var minPriceTempp = ""
    private var maxPriceTempp = ""
    private var minStarTempp = ""
    private var maxStarTempp = ""
    private var isFilterEnabled = false
    private lateinit var sharedPreferencesClass: SharedPreferencesClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val BEER = "beer"
        const val WINE = "wine"
        const val COCKTAIL = "cocktail"
        const val CUR_RUB = "RUB"
        const val CUR_EUR = "EUR"
        const val CUR_USD = "USD"
        const val C_RUS = "RUS"
        const val C_EU = "EU"
        const val C_USA = "USA"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialTablayout()
        initialRecyler()
        initialShared()
        checkLanguage()
        initialFlagSpinner()
        getID(BEER)
        categoryTemp = BEER
        initialSearch()
        (activity as AppCompatActivity).supportActionBar?.hide()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
        binding.searchBeerButton.setOnClickListener {
            showTopSheet()
        }
    }

    private fun showTopSheet() {
        val view = layoutInflater.inflate(R.layout.dialog_top_sheet, null)
        val dialog = context?.let { it1 ->
            BottomSheetDialog(
                it1,
                R.style.BottomSheetDialogTheme
            )
        }
        val countrySpinner = view.findViewById<Spinner>(R.id.countrySpinner)
        val minStar = view.findViewById<EditText>(R.id.minStarEditText)
        val maxStar = view.findViewById<EditText>(R.id.maxStarEditText)
        val beerTypeSpinner = view.findViewById<Spinner>(R.id.beerTypeSpinner)
        val searchButton = view.findViewById<Button>(R.id.searchButton)
        val cardStatus = view.findViewById<Switch>(R.id.cardStatusSwitch)
        val minPriceTw = view.findViewById<EditText>(R.id.minPriceText)
        val maxPrice = view.findViewById<EditText>(R.id.maxPriceText)
        val resetButton = view.findViewById<Button>(R.id.resetButton)

        if (isFilterEnabled) {
            minStar.text = Editable.Factory.getInstance().newEditable(minStarTempp)
            maxStar.text = Editable.Factory.getInstance().newEditable(maxStarTempp)
            minPriceTw.text = Editable.Factory.getInstance().newEditable(minPriceTempp)
            maxPrice.text = Editable.Factory.getInstance().newEditable(maxPriceTempp)
            countrySpinner.setSelection(countryPosition)
            beerTypeSpinner.setSelection(beerTypePosition)
            getCountryList(countrySpinner, countryPosition)
            getBeerTypeSpinner(beerTypeSpinner, beerTypePosition)
        } else {
            getCountryList(countrySpinner, 0)
            getBeerTypeSpinner(beerTypeSpinner, 0)
        }
        resetButton.setOnClickListener {
            getData(categoryTemp, id)
            countrySpinner.setSelection(0)
            minStar.text.clear()
            maxStar.text.clear()
            minPriceTw.text.clear()
            maxPrice.text.clear()
            beerTypeSpinner.setSelection(0)
            isFilterEnabled = false
            binding.searchBeerButton.setImageResource(R.drawable.ic_beer_filter)
        }

        searchButton.setOnClickListener {
            var minStarTemp = minStar.text.toString()
            var maxStarTemp = maxStar.text.toString()
            if (minStarTemp.isNullOrEmpty()) {
                minStarTemp = "0"
            }
            if (maxStarTemp.isNullOrEmpty()) {
                maxStarTemp = "10"
            }
            var minPriceTemp = minPriceTw.text.toString()
            var maxPriceTemp = maxPrice.text.toString()
            if (minPriceTemp.isNullOrEmpty()) {
                minPriceTemp = "0"
            }
            if (maxPriceTemp.isNullOrEmpty()) {
                maxPriceTemp = "1000"
            }
            countryPosition = countrySpinner.selectedItemPosition
            minStarTempp = minStarTemp
            maxStarTempp = maxStarTemp
            beerTypePosition = beerTypeSpinner.selectedItemPosition

            minPriceTempp = minPriceTemp
            maxPriceTempp = maxPriceTemp
            isFilterEnabled = true
            binding.searchBeerButton.setImageResource(R.drawable.ic_beer_filter_filtered)
            VM.getTopSheetSearchList(
                country = countrySpinner.selectedItem.toString(),
                minStar = minStarTemp,
                maxStar = maxStarTemp,
                userID = id,
                beerType = beerTypeSpinner.selectedItem.toString(),
                userCardStatus = cardStatus.isChecked.toString(),
                minPrice = minPriceTemp.toFloat(),
                maxPrice = maxPriceTemp.toFloat()
            ).observe(viewLifecycleOwner, Observer {
                initialRecyler()
                adapter.submitList(it)
            })
        }
        val closeButton = view.findViewById<Button>(R.id.closeButton)
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

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[BeerFragmentViewModel::class.java]
    }

    private fun initialShared() {
        sharedPreferencesClass = SharedPreferencesClass()
        context?.let { sharedPreferencesClass.instantPref(it) }

    }

    private fun getCountryList(spinner: Spinner, countryPosition: Int) {
        VM.getCountryList().observe(viewLifecycleOwner, Observer {
            val list = arrayListOf<String>("All")
            list.addAll(it)
            initCountrySpinner(list, spinner, countryPosition)
        })
    }

    private fun getBeerTypeSpinner(spinner: Spinner, beerTypePosition: Int) {
        VM.getCategoryList().observe(viewLifecycleOwner, Observer {
            val list = arrayListOf("All")
            list.addAll(it)
            initialBeerTypeSpinner(list, spinner, beerTypePosition)
        })
    }

    private fun getConvertedValue(price: String, price1: TextView) {
        val priceSplit = price.split(" ")
        checkLanguage()
        val cLanguage = Locale.getDefault().isO3Country
        var newFrom = ""
        var newTo = ""
        val currencyType = priceSplit.get(1)
        if (currencyType == CUR_RUB) {
            if (cLanguage == C_USA) {
                newFrom = CUR_RUB
                newTo = CUR_USD
            } else if (cLanguage == C_RUS) {
                newFrom = CUR_RUB
                newTo = CUR_RUB
            } else {
                newFrom = CUR_RUB
                newTo = CUR_EUR
            }
        } else if (currencyType == CUR_USD) {
            if (cLanguage == C_RUS) {
                newFrom = CUR_USD
                newTo = CUR_RUB
            } else if (cLanguage == C_USA) {
                newFrom = CUR_USD
                newTo = CUR_USD
            } else {
                newFrom = CUR_USD
                newTo = CUR_EUR
            }
        } else {
            if (cLanguage == C_USA) {
                newFrom = CUR_EUR
                newTo = CUR_USD
            } else if (cLanguage == C_USA) {
                newFrom = CUR_EUR
                newTo = CUR_RUB
            } else {
                newFrom = CUR_EUR
                newTo = CUR_EUR
            }
        }
        var result = ""
        if (newFrom != newTo) {
            VM.getConvertedValue(newFrom, newTo).observe(viewLifecycleOwner, Observer {
                if (cLanguage == C_RUS) {
                    result = (priceSplit.get(0).toString().toDouble() * it.toString()
                        .toDouble()).toString()
                } else if (cLanguage == C_USA && newTo == CUR_USD && newFrom == CUR_EUR) {
                    result = (priceSplit.get(0).toString().toDouble() * it.toString()
                        .toDouble()).toString()
                } else if (cLanguage != C_USA && cLanguage != C_RUS && newTo == CUR_EUR && newFrom == CUR_USD) {
                    result = (priceSplit.get(0).toString().toDouble() * it.toString()
                        .toDouble()).toString()
                } else if (cLanguage != C_USA && cLanguage != C_RUS && newTo == CUR_EUR && newFrom == CUR_RUB) {
                    result = (priceSplit.get(0).toString().toDouble() / it.toString()
                        .toDouble()).toString()
                } else if (cLanguage == C_USA && newFrom == CUR_RUB) {
                    result = (priceSplit.get(0).toString().toDouble() / it.toString()
                        .toDouble()).toString()
                }
                if (result.contains(".")) {
                    val resultSplit = result.split(".")
                    var newComma = ""
                    if (resultSplit.get(1).length > 2) {
                        newComma = resultSplit.get(1).substring(0, 2)
                    }
                    result = resultSplit.get(0) + "." + newComma
                }
                if (cLanguage == C_USA) {
                    result = "$result $CUR_USD"
                } else if (cLanguage == C_RUS) {
                    result = "$result $CUR_RUB"
                } else {
                    result = "$result $CUR_EUR"
                }
                price1.text = result
            })
        } else {
            result = priceSplit.get(0)
            if (cLanguage == C_USA) {
                result = "$result $CUR_USD"
            } else if (cLanguage == C_RUS) {
                result = "$result $CUR_RUB"
            } else {
                result = "$result $CUR_EUR"
            }
            price1.text = result
        }


    }

    private fun initCountrySpinner(list: List<String>, spinner: Spinner, countryPosition: Int) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item, list
            )
        }
        spinner.setBackgroundColor(Color.WHITE)
        spinner.adapter = adapter
        spinner.setSelection(countryPosition)
    }

    private fun initialBeerTypeSpinner(
        list: List<String>,
        spinner: Spinner,
        beerTypePosition: Int
    ) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item, list
            )
        }
        spinner.setBackgroundColor(Color.WHITE)
        spinner.adapter = adapter
        spinner.setSelection(beerTypePosition)
    }

    private fun initialFlagSpinner() {
        val list = arrayListOf<String>(C_RUS, C_EU, C_USA)
        val flagList =
            arrayListOf<Int>(R.drawable.russian_flag, R.drawable.eu_flag, R.drawable.american_flag)

        val adapter = SpinnerAdapterr(requireContext(), list, flagList)
        binding.companyLanguageSpinner.adapter = adapter
        //     binding.companyLanguageSpinner.orien
        if (sharedPreferencesClass.getCompanyLanguage().equals(C_USA)) {
            binding.companyLanguageSpinner.setSelection(2)
        } else if (sharedPreferencesClass.getCompanyLanguage().equals(C_EU)) {
            binding.companyLanguageSpinner.setSelection(1)

        } else {
            binding.companyLanguageSpinner.setSelection(0)

        }
        binding.companyLanguageSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                if (i == 0) {
                    language = C_RUS
                    setLanguage(C_RUS)
                    getID(categoryTemp)
                } else if (i == 1) {
                    language = C_EU
                    setLanguage(C_EU)
                    getID(categoryTemp)
                } else if (i == 2) {
                    language = C_USA
                    setLanguage(C_USA)
                    getID(categoryTemp)
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }
    }

    private fun checkLanguage() {
        if (sharedPreferencesClass.getCompanyLanguage().isNullOrEmpty()) {
            val currentLanguage = Locale.getDefault().isO3Country
            setLanguage(currentLanguage)
            language = currentLanguage
        } else {
            language = sharedPreferencesClass.getCompanyLanguage().toString()
        }
    }

    private fun setLanguage(language: String) {
        sharedPreferencesClass.setCompanyLanguage(language)
    }

    private fun initialSearch() {
        binding.searchBar.search_input_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length >= 3) {
                    searchData(s.toString())
                } else if (s.toString().length == 0) {
                    getData(categoryTemp, s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    private fun getData(category: String, s: String) {
        VM.getCards(category, s, language).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
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

    @SuppressLint("SetTextI18n")
    private fun checkClickedLayout(model: CardModel) {
        val view = layoutInflater.inflate(R.layout.detail_dialog_content, null)
        val dialog = context?.let { it1 ->
            BottomSheetDialog(
                it1,
                R.style.BottomSheetDialogTheme
            )
        }

        val recycler = view.findViewById<RecyclerView>(R.id.comments)
        commentAdapter = CommentRecyclerAdapter()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = commentAdapter
        VM.getCardComments(model.cardID).observe(viewLifecycleOwner, Observer {
            val list = it.sortedByDescending {
                it.commentTime
            }
            commentAdapter.submitList(list)
        })
        val closeButton = view.findViewById<ImageView>(R.id.dialogContentClose)
        val image = view.findViewById<ImageView>(R.id.dialogImagView)
        val name = view.findViewById<TextView>(R.id.alcoholName)
        val country = view.findViewById<TextView>(R.id.alcoholCountry)
        val city = view.findViewById<TextView>(R.id.alcoholCity)
        val info = view.findViewById<TextView>(R.id.alcoholInfo)
        val price = view.findViewById<TextView>(R.id.alcoholPrice)
        getConvertedValue(model.cardPrice.toString(), price)

        val voteTotal = view.findViewById<TextView>(R.id.voteTotal)
        val comment = view.findViewById<EditText>(R.id.sendComment)
        comment.visibility = View.GONE
        val commentButton = view.findViewById<ImageView>(R.id.sendCommentButton)
        commentButton.visibility = View.GONE
        name.text = model.cardName
        country.text = model.cardCounty
        city.text = model.cardCity
        info.text = model.cardInfo
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
        for (i in 0..averageRate - 1) {
            list.get(i).setImageResource(R.drawable.ic_dialog_rate_star_check)
        }
        for (t in averageRate..list.size - 1) {
            list.get(t).setImageResource(R.drawable.ic_dialog_noncheck_star)
        }
    }

    private fun initialTablayout() {
        binding.ltbeer.isSelected = true
        categoryTemp = BEER
        binding.ltbeer.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = true
            categoryTemp = BEER
            getID(BEER)
        }
        binding.ltwine.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = true
            binding.ltbeer.isSelected = false
            categoryTemp = WINE
            getID(WINE)
        }
        binding.ltcocktail.setOnClickListener {
            binding.ltcocktail.isSelected = true
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = false
            categoryTemp = COCKTAIL
            getID(COCKTAIL)
        }
    }
}