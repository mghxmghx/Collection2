package com.sezer.kirpitci.collection.ui.features.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentLoginBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.UserAct
import com.sezer.kirpitci.collection.utis.others.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentLoginBinding
    private lateinit var VM: LoginViewModel
    private lateinit var sharedPreferencesClass: SharedPreferencesClass
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        goToRegister()
        initialShared()
        loginClickListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        progressDialog = ProgressDialog(context)
        MyApp.appComponent.inject(this)
    }

    private fun initialShared() {
        sharedPreferencesClass = SharedPreferencesClass()
        context?.let { sharedPreferencesClass.instantPref(it) }

    }

    private fun goToRegister() {
        binding.loginRegisterTextview.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun loginClickListener() {
        binding.loginButton.setOnClickListener {
            onPreExecute()
            auth()
        }
    }

    private fun onPreExecute() {
        progressDialog.setMessage("Giriş yapılıyor..")
        progressDialog.show()
    }

    private fun onPostExecute() {
        progressDialog.dismiss()
    }

    private fun auth() {
        VM.auth(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
            .observe(viewLifecycleOwner,
                Observer {
                    if (it) {
                        if (binding.loginRememberMe.isChecked) {
                            sharedPreferencesClass.addUserEmail(binding.loginEmail.text.toString())
                            sharedPreferencesClass.addUserPassword(binding.loginPassword.text.toString())
                        }
                        VM.getStatus().observe(viewLifecycleOwner, Observer {
                            onPostExecute()
                            if (it.equals("admin")) {
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_loginFragment_to_adminPanelFragment)
                            } else if (it.equals("user")) {
                                val intent = Intent(activity, UserAct::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            } else {
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_loginFragment_to_homePageFragment)
                            }
                        })
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.wrong_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }
}