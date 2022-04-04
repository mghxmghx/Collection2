package com.sezer.kirpitci.collection.ui.features.user.home

import SpinnerAdapterr
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentUserBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.CommentRecyclerAdapter
import com.sezer.kirpitci.collection.utis.adapters.RecyclerAdapter
import com.sezer.kirpitci.collection.utis.others.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.updateWithUrlWithStatus
import kotlinx.android.synthetic.main.view_search.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class UserFragment : Fragment(), ClickItemUser {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var VM: UserViewModel
    private var categoryTemp = ""
    private var language = ""
    private lateinit var sharedPreferencesClass: SharedPreferencesClass
    private lateinit var commentAdapter: CommentRecyclerAdapter
    companion object {
        const val BEER_CATEGORY = "beer"
        const val WINE_CATEGORY = "wine"
        const val COCKTAIL_CATEGORY = "cocktail"

        const val RUS_COUNTRY = "RUS"
        const val EU_COUNTRY = "EU"
        const val USA_COUNTRY = "USA"

        const val CALC_MINUS = "minus"
        const val CALC_PLUS = "plus"

        const val FALSE = "false"
        const val TRUE = "true"

        const val DEFAULT = "default"
    }

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
        initialShared()
        checkLanguage()
        initialFlagSpinner()
        initialRecyler()
        getID(BEER_CATEGORY)
        initialSearch()
        initialTablayout()
        binding.layouttop.setBackgroundResource(R.drawable.bg_seach)
        binding.searchBar.setBackgroundResource(R.drawable.bg_seach)
        categoryTemp = BEER_CATEGORY
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialFlagSpinner() {
        val list = arrayListOf<String>(RUS_COUNTRY, EU_COUNTRY, USA_COUNTRY)
        val flagList =
            arrayListOf<Int>(R.drawable.russian_flag, R.drawable.eu_flag, R.drawable.american_flag)
        val adapter = SpinnerAdapterr(requireContext(), list, flagList)
        binding.companyLanguageSpinner.adapter = adapter
        if (sharedPreferencesClass.getCompanyLanguage().equals(USA_COUNTRY)) {
            binding.companyLanguageSpinner.setSelection(2)
        } else if (sharedPreferencesClass.getCompanyLanguage().equals(EU_COUNTRY)) {
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
                    language = RUS_COUNTRY
                    setLanguage(RUS_COUNTRY)
                    getID(categoryTemp)
                } else if (i == 1) {
                    language = EU_COUNTRY
                    setLanguage(EU_COUNTRY)
                    getID(categoryTemp)
                } else if (i == 2) {
                    language = USA_COUNTRY
                    setLanguage(USA_COUNTRY)
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
                    searchData(alcoholName = s.toString())
                } else if (s.toString().length == 0) {
                    getID(categoryTemp)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setStatus(totalCount: Int, totalTrueStatus: Int) {
        binding.customProgress2.max = totalCount
        val currentProgress2 = totalTrueStatus
        binding.progress2NumberText.text = "$totalTrueStatus/$totalCount"
        ObjectAnimator.ofInt(binding.customProgress2, "progress", currentProgress2)
            .setDuration(1000)
            .start()
    }

    private fun countAlcoholStatus(list: List<CardModel>?, isCheckted: String) {
        var totalCount = 0
        var totalTrueStatus = 0
        if (isCheckted.equals(CALC_PLUS)) {
            val text = binding.progress2NumberText.text.split("/")
            totalCount = text.get(1).toInt()
            totalTrueStatus = text.get(0).toInt() + 1
        } else if (isCheckted.equals(CALC_MINUS)) {
            val text = binding.progress2NumberText.text.split("/")
            totalCount = text.get(1).toInt()
            totalTrueStatus = text.get(0).toInt() - 1
        } else {
            if (list != null) {
                for (i in 0 until list.size) {
                    totalCount++
                    if (list.get(i).status.equals(TRUE)) {
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

    private fun initialUI() {
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

    fun hideSoftKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getData(category: String, id: String) {
        VM.getCards(category, id, language).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            countAlcoholStatus(it, DEFAULT)
        })
    }

    private fun searchData(alcoholName: String) {
        VM.searchCards(alcoholName, categoryTemp, id).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initialShared() {
        sharedPreferencesClass = SharedPreferencesClass()
        context?.let { sharedPreferencesClass.instantPref(it) }

    }

    private fun isCheckVM(checked: Boolean, model: CardModel) {
        VM.setCheck(checked, model, id).observe(viewLifecycleOwner, Observer {
            initialRecyler()
            getData(categoryTemp, id)
        })
    }

    override fun clicked(model: CardModel) {
        getCardDetailsForBottomSheet(model.cardID)
    }

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
            commentAdapter.submitList(it)
        })
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
        val isCheck = view.findViewById<Switch>(R.id.isCheckForAlcohol)
        startOne.setOnClickListener {
            isCheck.isChecked = true
            starControl(1, view, model)
        }
        startTwo.setOnClickListener {
            isCheck.isChecked = true
            starControl(2, view, model)
        }
        startThree.setOnClickListener {
            isCheck.isChecked = true
            starControl(3, view, model)
        }
        startFour.setOnClickListener {
            isCheck.isChecked = true
            starControl(4, view, model)
        }
        startFive.setOnClickListener {
            isCheck.isChecked = true
            starControl(5, view, model)
        }
        startSix.setOnClickListener {
            isCheck.isChecked = true
            starControl(6, view, model)
        }
        startSeven.setOnClickListener {
            isCheck.isChecked = true
            starControl(7, view, model)
        }
        startEight.setOnClickListener {
            isCheck.isChecked = true
            starControl(8, view, model)
        }
        startNine.setOnClickListener {
            isCheck.isChecked = true
            starControl(9, view, model)
        }
        startTen.setOnClickListener {
            isCheck.isChecked = true
            starControl(10, view, model)

        }
        val image = view.findViewById<ImageView>(R.id.dialogImagView)
        val nameTw = view.findViewById<TextView>(R.id.alcoholName)
        val countryTw = view.findViewById<TextView>(R.id.alcoholCountry)
        val priceTw = view.findViewById<TextView>(R.id.alcoholPrice)
        val cityTw = view.findViewById<TextView>(R.id.alcoholCity)
        val infoTw = view.findViewById<TextView>(R.id.alcoholInfo)
        val voteCount = view.findViewById<TextView>(R.id.voteTotal)
        voteCount.isVisible = false
        nameTw.text = model.cardName
        countryTw.text = model.cardCounty
        priceTw.text = model.cardPrice
        cityTw.text = model.cardCity
        infoTw.text = model.cardInfo
        if (!model.userStarRate.isNullOrEmpty() && !model.userStarRate.toString().equals("null")) {
            setBackGrounds(model.userStarRate!!.toInt(), view, model)
        } else {
            setBackGrounds(0, view, model)

        }

        isCheck.isVisible = true
        isCheck.isChecked = model.status.toBoolean()

        isCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isCheck.isChecked) {
                isCheckVM(true, model)
                countAlcoholStatus(null, CALC_PLUS)
            } else {
                isCheckVM(false, model)
                countAlcoholStatus(null, CALC_MINUS)

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

    private fun getCardDetailsForBottomSheet(cardId: String) {
        VM.getCardDetails(cardId, id).observe(viewLifecycleOwner, Observer {
            checkClickedLayout(it)
        })
    }

    private fun starControl(i: Int, view: View, model: CardModel) {
        setBackGrounds(i, view, model)
        val oldVote = model.userStarRate
        val newVote = i.toString()
        model.userStarRate = i.toString()
        Log.d("TAG", "starControl: " + model.voteCount)
        Log.d("TAG", "starControl: " + model.userVoted)
        if (model.userVoted.toString().equals("null") || model.userVoted.toString().equals(FALSE)) {
            model.voteCount = (model.voteCount?.toInt()?.plus(1)).toString()
            model.userVoted = true.toString()
        }
        Log.d("TAG", "starControl: " + model.voteCount)
        Log.d("TAG", "starControl: " + model.userVoted)
        setStarInFB(model, oldVote, newVote)
    }

    private fun setBackGrounds(clickedNumber: Int, view: View, model: CardModel) {
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
        for (i in 0 until clickedNumber) {
            list.get(i).setImageResource(R.drawable.ic_dialog_rate_star_check)
        }
        for (t in clickedNumber..9) {
            list.get(t).setImageResource(R.drawable.ic_dialog_noncheck_star)
        }
    }

    private fun setStarInFB(model: CardModel, oldVote: String?, newVote: String) {
        VM.setStarInFB(model, id, oldVote, newVote)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searhQuery: String?): Boolean {
                if (searhQuery != null) {
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun initialTablayout() {
        binding.ltbeer.isSelected = true
        categoryTemp = BEER_CATEGORY
        binding.ltbeer.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = true
            binding.searchBar.search_input_text.text?.clear()
            categoryTemp = BEER_CATEGORY
            getID(BEER_CATEGORY)
        }
        binding.ltwine.setOnClickListener {
            binding.ltcocktail.isSelected = false
            binding.ltwine.isSelected = true
            binding.ltbeer.isSelected = false
            binding.searchBar.search_input_text.text?.clear()
            categoryTemp = WINE_CATEGORY
            getID(WINE_CATEGORY)
        }
        binding.ltcocktail.setOnClickListener {
            binding.ltcocktail.isSelected = true
            binding.ltwine.isSelected = false
            binding.ltbeer.isSelected = false
            binding.searchBar.search_input_text.text?.clear()
            categoryTemp = COCKTAIL_CATEGORY
            getID(COCKTAIL_CATEGORY)
        }
    }
}