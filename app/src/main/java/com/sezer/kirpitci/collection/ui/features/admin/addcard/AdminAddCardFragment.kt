package com.sezer.kirpitci.collection.ui.features.admin.addcard

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
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
import java.io.ByteArrayOutputStream
import javax.inject.Inject

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
        super.onViewCreated(view, savedInstanceState)

    }
    private fun initialUI(){
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

    private fun clickListener() {
        binding.button2.setOnClickListener {
            VM.getMaxId().observe(viewLifecycleOwner, Observer {

                val cardID = (it + 1).toString()
                val cardName = binding.addCardNameText.text.toString()
                val cardInfo = binding.cardInfoText.text.toString()
                val cardCategory = binding.cardCategoryText.text.toString()
                val cardCountry = binding.cardCountryText.text.toString()
                val cardCity = binding.cardCityText.text.toString()
                val cardPrice = binding.cardPriceText.text.toString()

                if (!cardName.isEmpty() && !cardCategory.isEmpty() && !cardCountry.isEmpty()) {
                    if (uri.equals("")) {
                        uri = default
                    }
                    addCard(
                        AddCardModel(
                            cardID,
                            cardName,
                            cardInfo,
                            cardCategory,
                            cardCountry,
                            cardCity,
                            cardPrice,
                            uri
                        )
                    )

                }
            })

        }

    }

    private fun addCard(model: AddCardModel) {
        VM.setChildImage(model.cardPath.toUri(),model.cardID).observe(viewLifecycleOwner, Observer {
            model.cardPath = it
            VM.addCard(model).observe(viewLifecycleOwner, Observer {
                
            })
        })
        VM.addCard(model).observe(viewLifecycleOwner, Observer {
            if (it) {
                /*         binding.addCardNameText.setText("")
                          binding.cardCategoryText.setText("")
                          binding.cardCityText.setText("")
                          binding.cardCountryText.setText("")
                          binding.cardPriceText.setText("")
                          binding.cardInfoText.setText("")
                          binding.imageView2.resetImage(binding.imageView2)*/
                Toast.makeText(context,"asd",Toast.LENGTH_SHORT).show()


            }
        })

    }


}