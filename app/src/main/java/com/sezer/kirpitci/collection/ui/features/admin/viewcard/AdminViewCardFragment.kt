package com.sezer.kirpitci.collection.ui.features.admin.viewcard

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentAdminViewCardBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.utis.adapters.AdapterX
import com.sezer.kirpitci.collection.utis.adapters.AdminViewCardAdapter
import com.sezer.kirpitci.collection.utis.adapters.ClickListener
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.others.updateWithUrl
import java.sql.Timestamp
import javax.inject.Inject


class AdminViewCardFragment : Fragment(), ClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentAdminViewCardBinding
    private lateinit var adapter: AdminViewCardAdapter
    private lateinit var VM: AdminViewCardViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var view1: View
    private var uri: String = ""

    companion object {
        const val DEFAULT = "default"
        const val CATEGORY_WINE = "Wine"
        const val CATEGORY_BEER = "Beer"
        const val CATEGORY_COCKTAIL = "Cocktail"
    }

    private var companyList = listOf<String>()
    private var categoryList = listOf<String>()
    private var countryList = listOf<String>()
    private var typeList = listOf<String>()
    var adapterX = AdapterX(ArrayList<ViewCardModel>(), this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminViewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        progressDialog = ProgressDialog(context)
        getData()
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        initialRecyler()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initialRecyler() {
        binding.cardRecycler.layoutManager = LinearLayoutManager(context)
        binding.cardRecycler.adapter = adapterX
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[AdminViewCardViewModel::class.java]
    }

    fun getData() {
        VM.getCards().observe(viewLifecycleOwner, Observer {
            adapterX.swap(it)
        })
    }

    private fun updateCard(model: ViewCardModel) {
        view1 = layoutInflater.inflate(R.layout.dialog_card_update, null)

        val dialog = context?.let { it1 ->
            BottomSheetDialog(
                it1,
                R.style.BottomSheetDialogTheme
            )
        }
        val cardCompany = view1.findViewById<Spinner>(R.id.card_company)
        val cartTypeSpinner = view1.findViewById<Spinner>(R.id.card_type)
        val cardCountry = view1.findViewById<Spinner>(R.id.card_country)
        getCategoryList(cartTypeSpinner, model.cardCategory)
        getCountryList(cardCountry, model.cardCounty)
        getCompanyList(cardCompany, model.cardCompany)
        val cardName = view1.findViewById<EditText>(R.id.add_card_name_text)
        cardName.text = Editable.Factory.getInstance().newEditable(model.cardName)
        val cardInfo = view1.findViewById<EditText>(R.id.card_info_text)
        cardInfo.text = Editable.Factory.getInstance().newEditable(model.cardInfo)

        val cardPrice = view1.findViewById<EditText>(R.id.card_price_text)
        cardPrice.text = Editable.Factory.getInstance().newEditable(model.cardPrice.toString().split(" ").get(0))
        val cardImage = view1.findViewById<ImageView>(R.id.imageView2)
        val cardABV = view1.findViewById<EditText>(R.id.card_abv_text)
        cardABV.text = Editable.Factory.getInstance().newEditable(model.cardABV)
        val cardML = view1.findViewById<EditText>(R.id.card_ML_text)
        cardML.text = Editable.Factory.getInstance().newEditable(model.beerML.toString())

        val rubcur = view1.findViewById<RadioButton>(R.id.rubcur)
        val usdcur = view1.findViewById<RadioButton>(R.id.usdcur)
        val eurcur = view1.findViewById<RadioButton>(R.id.eurcur)

        val checkboxUSA = view1.findViewById<CheckBox>(R.id.checkboxUSA)
        val checkboxEUR = view1.findViewById<CheckBox>(R.id.checkboxEU)
        val checkboxRU = view1.findViewById<CheckBox>(R.id.checkBoxRU)



        val priceCur = model.cardPrice.toString().split(" ").get(1)
        if(priceCur == "RUB"){
            rubcur.isChecked = true
            usdcur.isChecked = false
            eurcur.isChecked = false
        } else if(priceCur == "USD"){
            rubcur.isChecked = false
            usdcur.isChecked = true
            eurcur.isChecked = false
        } else {
            rubcur.isChecked = false
            usdcur.isChecked = false
            eurcur.isChecked = true
        }
        val country = model.beerInCountry.split(",")
        for (i in country.indices){
            if(country[i].equals("RUS")){
                checkboxRU.isChecked = true
            }
            if(country[i].equals("EUR")){
                checkboxEUR.isChecked = true
            }
            if(country[i].equals("USA")){
                checkboxUSA.isChecked = true
            }
        }
        cardImage.setOnClickListener {
            openImage()
        }

        cardImage.updateWithUrl(model.cardPath, cardImage)
       // val closeButton = view1.findViewById<ImageView>(R.id.imageView)
      /*  closeButton.setOnClickListener {
            if (dialog != null) {
                (view1.parent as ViewGroup).removeView(view1)
                dialog.dismiss()
            }
        }*/
        val updateButton = view1.findViewById<Button>(R.id.button2)
        updateButton.setOnClickListener {
            var cardPrice = cardPrice.text.toString()
            var beerInCountry = ""
            if(rubcur.isChecked){
                cardPrice = cardPrice + " RUB"
            } else if(usdcur.isChecked) {
                cardPrice = cardPrice + " USD"

            } else {
                cardPrice = cardPrice + " EUR"
            }
            if(checkboxUSA.isChecked) {
                beerInCountry = beerInCountry + "," + "USA"

            }
            if (checkboxEUR.isChecked) {
                beerInCountry = beerInCountry + "," + "EUR"

            }
            if (checkboxRU.isChecked) {
                beerInCountry = beerInCountry + "," + "RUS"

            }
            val newModel = ViewCardModel(
                cardID = model.cardID,
                cardName = cardName.text.toString(),
                cardInfo = cardInfo.text.toString(),
                cardCategory = cartTypeSpinner.selectedItem.toString(),
                cardCounty = cardCountry.selectedItem.toString(),
                cardPrice = cardPrice,
                cardABV = cardABV.text.toString(),
                beerML = cardML.text.toString(),
                cardCompany = cardCountry.selectedItem.toString(),
                cardPath = model.cardPath,
                beerInCountry = beerInCountry,
                cardAverage = model.cardAverage,
                voteCount = model.voteCount,
                cardType = cartTypeSpinner.selectedItem.toString()
            )
            updateImage(newModel)
            if (dialog != null) {
                (view1.parent as ViewGroup).removeView(view1)
                getData()
                dialog.dismiss()
            }
        }
        if (dialog != null) {
            dialog.setCancelable(true)
        }
        if (dialog != null) {

            dialog.setContentView(view1)
        }
        if (dialog != null) {
            dialog.show()
        }
    }

    override fun itemUpdateClick(data: ViewCardModel) {
        updateCard(data)
    }

    override fun itemDeleteClick(data: ViewCardModel) {
        VM.deleteChildren(data)
        getData()
    }
    private fun getCategoryList(spinner: Spinner, cardCategory: String) {
        VM.getCategoryList().observe(viewLifecycleOwner, Observer {
            categoryList = it
            initTypeSpinner(it, spinner, cardCategory)
            initCategorySpinner(spinner)
        })
    }

    private fun getCompanyList(spinner: Spinner, cardCompany: String) {
        VM.getCompanyList().observe(viewLifecycleOwner, Observer {
            companyList = it
            initCompanySpinner(it, spinner, cardCompany)
        })
    }

    private fun getCountryList(spinner: Spinner, cardCounty: String) {
        VM.getCountryList().observe(viewLifecycleOwner, Observer {
            countryList = it
            initCountrySpinner(it, spinner, cardCounty)
        })
    }
    private fun initCategorySpinner(spinner: Spinner) {
        val listx = arrayListOf(CATEGORY_BEER, CATEGORY_WINE, CATEGORY_COCKTAIL)
        typeList = listx
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, listx
            )
        }
        spinner.setBackgroundColor(Color.WHITE)
        spinner.adapter = adapter
    }

    private fun initCompanySpinner(list: List<String>, spinner: Spinner, cardCompany: String) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list[i])
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, list
            )
        }
        spinner.setBackgroundColor(Color.WHITE)

        spinner.adapter = adapter
        spinner.setSelection(list.indexOf(cardCompany))

    }
    private fun initTypeSpinner(list: List<String>, spinner: Spinner, cardCategory: String) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, list
            )
        }
        spinner.setBackgroundColor(Color.WHITE)
        spinner.adapter = adapter
        spinner.setSelection(list.indexOf(cardCategory))

    }

    private fun initCountrySpinner(list: List<String>, spinner: Spinner, cardCounty: String) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, list
            )
        }
        spinner.setBackgroundColor(Color.WHITE)
        spinner.adapter = adapter
        spinner.setSelection(list.indexOf(cardCounty))
    }
    private fun openImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/* video/*"
        //intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
        progressDialog.setMessage(getString(R.string.fragment_add_card_loading))
        progressDialog.show()
    }

    private fun updateImage(model: ViewCardModel) {
        if (!model.cardName.isEmpty() && !model.cardCategory.isEmpty() && !model.cardCounty.isEmpty()) {
            if (uri.equals("")) {
                uri = DEFAULT
            }
            if (!uri.equals(DEFAULT)) {
                VM.setChildImage(uri.toUri(), Timestamp(System.currentTimeMillis()).toString())
                    .observe(viewLifecycleOwner, Observer {
                        model.cardPath = it
                        VM.updateCard(model).observe(viewLifecycleOwner, Observer {
                            getData()
                            uri = ""
                        })
                    })
            } else {
                VM.updateCard(model).observe(viewLifecycleOwner, Observer {
                    getData()
                    uri = ""
                })
            }
        } else {
            Toast.makeText(context, getString(R.string.wrong_message), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data?.data != null) {
            uri = data.data.toString()
            val x = view1.findViewById<ImageView>(R.id.imageView2)
            x.setImageURI(data.data)
        }
        progressDialog.dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }
}