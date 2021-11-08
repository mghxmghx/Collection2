package com.sezer.kirpitci.collection.ui.features.admin.addcard

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.databinding.FragmentAdminAddCardBinding
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel
import com.sezer.kirpitci.collection.utis.AddCardViewModelFactory
import com.sezer.kirpitci.collection.utis.ViewModelFactory
import com.sezer.kirpitci.collection.utis.resetImage
import kotlin.math.log

class AdminAddCardFragment : Fragment() {
    private lateinit var binding: FragmentAdminAddCardBinding
    private lateinit var progressDialog: ProgressDialog
    private var uri: String = ""
    private lateinit var VM:AdminAddCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressDialog = ProgressDialog(context)
        imageClickListener()
        initialVM()
        clickListener()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun imageClickListener() {
        binding.imageView2.setOnClickListener {
            openImage()
        }

    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
        progressDialog.setMessage("Loading")
        progressDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data?.data != null) {
            uri = data.data.toString()
            binding.imageView2.setImageURI(data.data)
        }
        progressDialog.dismiss()

        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun initialVM() {
        val factory = AddCardViewModelFactory()
        VM = ViewModelProvider(this, factory)[AdminAddCardViewModel::class.java]

    }
    private fun clickListener(){
        binding.button2.setOnClickListener{
            val cardID=binding.cardIdText.text.toString()
            val cardName=binding.addCardNameText.text.toString()
            val cardInfo=binding.cardInfoText.text.toString()
            val cardCategory=binding.cardCategoryText.text.toString()
            val cardCountry=binding.cardCountryText.text.toString()
            val cardCity=binding.cardCityText.text.toString()
            val cardPrice=binding.cardPriceText.text.toString()

            if(!cardID.isEmpty() && !cardName.isEmpty() && !cardCategory.isEmpty() && !cardCountry.isEmpty())
            {
                if(uri.equals(""))
                {
                    uri="default"
                }
                VM.setChildImage(uri.toUri(),cardID)
                    .observe(viewLifecycleOwner, Observer {
                        if(it.equals("default"))
                        {
                            addCard(AddCardModel(cardID,cardName,cardInfo,cardCategory,cardCountry,cardCity,cardPrice,"default"
                            ))
                        }
                        else{
                            addCard(AddCardModel(cardID,cardName,cardInfo,cardCategory,cardCountry,cardCity,cardPrice,it.toString()
                            ))
                        }

                    })
            }

        }

    }
    private fun addCard(model:AddCardModel){
        VM.addCard(model).observe(viewLifecycleOwner, Observer {
            if(it){
                binding.cardIdText.setText("")
                binding.addCardNameText.setText("")
                binding.cardCategoryText.setText("")
                binding.cardCityText.setText("")
                binding.cardCountryText.setText("")
                binding.cardPriceText.setText("")
                binding.cardInfoText.setText("")
                binding.imageView2.resetImage(binding.imageView2)


            }
        })

    }


}