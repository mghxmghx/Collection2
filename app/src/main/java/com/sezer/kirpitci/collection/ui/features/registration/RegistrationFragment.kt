package com.sezer.kirpitci.collection.ui.features.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.database.DatabaseReference
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentRegistrationBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject


class RegistrationFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var VM: RegistrationViewModel

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        clickListeners()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]

    }

    private fun clickListeners() {
        binding.button.setOnClickListener {
            if (!binding.mail.text.toString().isEmpty() && !binding.password.text.toString()
                    .isEmpty()
            ) {
                if (binding.password.text.toString()
                        .equals(binding.confirmPassword.text.toString())
                ) {
                    getMaxID()
                } else {
                    getString(R.string.notConfirm)
                }
            } else {
                Toast.makeText(context, getString(R.string.Empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMaxID() {
        VM.getMaxId().observe(viewLifecycleOwner, Observer {
            createUser(it)
        })
    }

    private fun createUser(i: Int) {
        val model = CreateUserModel(binding.mail.text.toString(), binding.password.text.toString())
        VM.createUser(model)
            .observe(viewLifecycleOwner, Observer {
                if (it) {
                    addUserStatus(
                        AddUserModel(
                            binding.mail.text.toString(),
                            binding.name.text.toString(),
                            "user",
                            (i + 1).toString()
                        )
                    )
                } else {
                    Toast.makeText(context, getString(R.string.Create_Fail), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun addUserStatus(model: AddUserModel) {
        VM.createStatus(model).observe(viewLifecycleOwner, Observer {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_registrationFragment_to_loginFragment)
            } else {
                Toast.makeText(context, getString(R.string.Create_Fail), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}