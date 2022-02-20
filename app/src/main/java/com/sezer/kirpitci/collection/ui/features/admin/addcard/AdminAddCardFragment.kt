package com.sezer.kirpitci.collection.ui.features.admin.addcard

import android.Manifest
import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.databinding.FragmentAdminAddCardBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.utis.default
import com.sezer.kirpitci.collection.utis.intentType
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import com.sezer.kirpitci.collection.utis.resetImage
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AdminAddCardFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentAdminAddCardBinding
    private lateinit var progressDialog: ProgressDialog
    private var uri: String = ""
    private lateinit var VM: AdminAddCardViewModel
    private val IMAGE_REQUEST: Int = 1
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 7

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressDialog = ProgressDialog(context)
        initialUI()
        imageClickListener()
        initialVM()
        clickListener()
        getCategoryList()
        getCompanyList()
        getCountryList()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }


    private fun requestPermissions(): Boolean {
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissionsNeeded.add(Manifest.permission.CAMERA)

        listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data?.data != null) {
            uri = data.data.toString()
            binding.imageView2.setImageURI(data.data)
        }
        progressDialog.dismiss()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun imageClickListener() {
        binding.imageView2.setOnClickListener {
            if (checkAndRequestPermissions()) {
                openImage()
            } else {
                requestPermissions()
            }
        }

    }

    private fun checkAndRequestPermissions(): Boolean {
        val wtite = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val read =
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true

    }

    private fun openImage() {
        val intent = Intent()
        intent.type = intentType
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
        progressDialog.setMessage("Loading")
        progressDialog.show()
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[AdminAddCardViewModel::class.java]

    }

    private var companyList = listOf<String>()

    private var categoryList = listOf<String>()

    private var countryList = listOf<String>()

    private var typeList = listOf<String>()

    private fun clickListener() {
        binding.button2.setOnClickListener {
            VM.getMaxId().observe(viewLifecycleOwner, Observer {
                val cardID = (it + 1).toString()
                val cardName = binding.addCardNameText.text.toString()
                val cardInfo = binding.cardInfoText.text.toString()
                val cardCategory =
                    categoryList.get(binding.cardCategory.selectedItemPosition).toString()
                val cardType = typeList.get(binding.cardType.selectedItemPosition)
                val cardCountry =
                    countryList.get(binding.cardCountry.selectedItemPosition).toString()
                val cardPrice = binding.cardPriceText.text.toString()
                val cardABV = binding.cardAbvText.text.toString()
                val cardCompany =
                    companyList.get(binding.cardCompany.selectedItemPosition).toString()
                if (!cardName.isEmpty() && !cardCategory.isEmpty() && !cardCountry.isEmpty()) {
                    if (uri.equals("")) {
                        uri = default
                    }
                    addCard(
                        AddCardModel(
                            cardID = cardID,
                            cardName = cardName,
                            cardInfo = cardInfo,
                            cardCategory = cardType.toLowerCase(Locale.getDefault()),
                            cardCounty = cardCountry,
                            cardCity = "",
                            cardPrice = cardPrice,
                            cardABV = cardABV,
                            cardPath = uri,
                            cardAverage = "0",
                            voteCount = "0",
                            cardCompany = cardCompany,
                            cardType = cardCategory
                        )
                    )

                }
            })

        }

    }

    private fun getUserList(cardID: String) {
        VM.checkUserList().observe(viewLifecycleOwner, Observer {
            addUsersUnderCard(it, cardID)
        })
    }

    private fun addUsersUnderCard(list: List<AddCardUserModel>, cardID: String) {
        VM.addUserUnderCard(list, cardID).observe(viewLifecycleOwner, Observer {
            binding.addCardNameText.setText("")
            //   binding.cardCategoryText.setText("")
            ///     binding.cardCityText.setText("")
            //    binding.cardCompanyText.setText("")
            //   binding.cardTypeText.setText("")
            binding.cardAbvText.setText("")
            //  binding.cardCountryText.setText("")
            binding.cardPriceText.setText("")
            binding.cardInfoText.setText("")
            binding.imageView2.resetImage(binding.imageView2)
            Toast.makeText(context, "asd", Toast.LENGTH_SHORT).show()
        })
    }

    private fun getCategoryList() {
        VM.getCategoryList().observe(viewLifecycleOwner, Observer {
            categoryList = it
            initTypeSpinner(it)
            initCategorySpinner()
        })
    }

    private fun getCompanyList() {
        VM.getCompanyList().observe(viewLifecycleOwner, Observer {
            companyList = it
            initCompanySpinner(it)
        })
    }

    private fun getCountryList() {
        VM.getCountryList().observe(viewLifecycleOwner, Observer {
            countryList = it
            initCountrySpinner(it)
        })
    }

    private fun initCategorySpinner() {
        val listx = arrayListOf("Beer", "Wine", "Cocktail")
        typeList = listx
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_dropdown_item, listx
            )
        }
        binding.cardCategory.setBackgroundColor(Color.WHITE)
        binding.cardCategory.adapter = adapter
    }

    private fun initTypeSpinner(list: List<String>) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_dropdown_item, list
            )
        }
        binding.cardType.setBackgroundColor(Color.WHITE)
        binding.cardType.adapter = adapter
    }

    private fun initCompanySpinner(list: List<String>) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_dropdown_item, list
            )
        }
        binding.cardCompany.setBackgroundColor(Color.WHITE)

        binding.cardCompany.adapter = adapter
    }

    private fun initCountrySpinner(list: List<String>) {
        val listx = arrayListOf<String>()
        for (i in 0 until listx.size) {
            listx.add(list.get(i))
        }
        list.toTypedArray()
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.simple_spinner_dropdown_item, list
            )
        }
        binding.cardCountry.setBackgroundColor(Color.WHITE)
        binding.cardCountry.adapter = adapter
    }

    private fun addCard(model: AddCardModel) {
        VM.setChildImage(model.cardPath.toUri(), model.cardID)
            .observe(viewLifecycleOwner, Observer {
                model.cardPath = it
                VM.addCard(model).observe(viewLifecycleOwner, Observer {
                    getUserList(model.cardID)
                })
            })
    }
}