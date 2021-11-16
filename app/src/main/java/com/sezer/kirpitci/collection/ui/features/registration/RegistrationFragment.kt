package com.sezer.kirpitci.collection.ui.features.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentRegistrationBinding
import com.sezer.kirpitci.collection.utis.RegistrationViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User


class RegistrationFragment : Fragment() {
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
        initialVM()
        clickListeners()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialVM() {
        val factory = RegistrationViewModelFactory()
        VM = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]

    }

    private fun clickListeners() {
        binding.button.setOnClickListener {
            if (!binding.mail.text.toString().isEmpty() && !binding.password.text.toString()
                    .isEmpty()
            ) {
                createUser()
            } else {
                Toast.makeText(context, getString(R.string.Empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser() {
        val model = CreateUserModel(binding.mail.text.toString(), binding.password.text.toString())

        VM.createUser(model)
            .observe(viewLifecycleOwner, Observer {


                if (it) {
                    addCards(AddUserModel(binding.mail.text.toString(),binding.name.text.toString(),"user",null))

                } else {
                    Toast.makeText(context, getString(R.string.Create_Fail), Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }
    private fun addCards(model:AddUserModel){
        VM.getCardNames().observe(viewLifecycleOwner, Observer {
            model.cards=it
            addUserStatus(model)
        })
    }

    private fun addUserStatus(model:AddUserModel){
        VM.createStatus(model).observe(viewLifecycleOwner, Observer {
            if(it){
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_registrationFragment_to_loginFragment)
            }
            else {
                Toast.makeText(context, getString(R.string.Create_Fail), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}