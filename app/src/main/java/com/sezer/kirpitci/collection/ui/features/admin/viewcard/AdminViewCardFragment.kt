package com.sezer.kirpitci.collection.ui.features.admin.viewcard

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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
import com.sezer.kirpitci.collection.utis.updateWithUrl
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
        //adapter = AdminViewCardAdapter(this)
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
        val cardName = view1.findViewById<EditText>(R.id.add_card_name_text)
        cardName.text = Editable.Factory.getInstance().newEditable(model.cardName)
        val cardCountry = view1.findViewById<EditText>(R.id.card_country_text)
        cardCountry.text = Editable.Factory.getInstance().newEditable(model.cardCounty)
        val cardInfo = view1.findViewById<EditText>(R.id.card_info_text)
        cardInfo.text = Editable.Factory.getInstance().newEditable(model.cardInfo)
        val cardCity = view1.findViewById<EditText>(R.id.card_city_text)
        cardCity.text = Editable.Factory.getInstance().newEditable(model.cardCity)
        val cardCategory = view1.findViewById<EditText>(R.id.card_category_text)
        cardCategory.text = Editable.Factory.getInstance().newEditable(model.cardCategory)
        val cardPrice = view1.findViewById<EditText>(R.id.card_price_text)
        cardPrice.text = Editable.Factory.getInstance().newEditable(model.cardPrice)
        val cardImage = view1.findViewById<ImageView>(R.id.imageView2)

        cardImage.setOnClickListener {
            openImage()
        }

        cardImage.updateWithUrl(model.cardPath, cardImage)
        val closeButton = view1.findViewById<ImageView>(R.id.imageView)
        closeButton.setOnClickListener {
            if (dialog != null) {
                (view1.parent as ViewGroup).removeView(view1)
                dialog.dismiss()
            }
        }
        val updateButton = view1.findViewById<Button>(R.id.update_button)
        updateButton.setOnClickListener {

            val newModel = ViewCardModel(
                model.cardID,
                cardName.text.toString(),
                cardInfo.text.toString(),
                cardCategory.text.toString(),
                cardCountry.text.toString(),
                cardCity.text.toString(),
                cardPrice.text.toString(),
                model.cardPath
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

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
        progressDialog.setMessage(getString(R.string.fragment_add_card_loading))
        progressDialog.show()
    }

    private fun updateImage(model: ViewCardModel) {
        if (!model.cardName.isEmpty() && !model.cardCategory.isEmpty() && !model.cardCounty.isEmpty()) {
            if (uri.equals("")) {
                uri = "default"
            }
            if (!uri.equals("default")) {
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