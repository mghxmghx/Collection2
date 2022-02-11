package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sezer.kirpitci.collection.databinding.PersonalInfoFragmentBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.MainActivity
import com.sezer.kirpitci.collection.utis.others.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class PersonalInfo : Fragment() {

    private lateinit var binding: PersonalInfoFragmentBinding
    private lateinit var viewModel: PersonalInfoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var sharedPreferencesClass: SharedPreferencesClass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PersonalInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        getUserId()
        clickListeners()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        viewModel = ViewModelProvider(this, viewModelFactory)[PersonalInfoViewModel::class.java]
    }

    private fun getUserId() {
        viewModel.getUserID().observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                getUserDetail(it)
            }
        })
    }

    private fun clickListeners() {
        binding.changePassword.setOnClickListener {
            if (binding.newPassword.text.toString().length >= 6) {
                changePassword(binding.newPassword.text.toString())
            }
        }
        binding.deleteUser.setOnClickListener {
            deleteUser()
        }
        binding.logout.setOnClickListener {
            logOut()
        }
    }

    private fun changePassword(newPassword: String) {
        viewModel.changePassword(newPassword).observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Pasword Changed!", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun deleteUser() {
        viewModel.deleteAccount().observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.deleteAccountRTDB(binding.userID.text.toString()).observe(
                    viewLifecycleOwner, Observer {
                        Toast.makeText(context, "Your account deleted!", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                )
            }
        })
    }

    private fun logOut() {
        initialShared()
        logoutSet()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun initialShared() {
        sharedPreferencesClass = SharedPreferencesClass()
        //sharedPreferencesClass.instantPref()
        context?.let { sharedPreferencesClass.instantPref(it) }

    }

    private fun logoutSet() {
        sharedPreferencesClass.addUserEmail("")
        sharedPreferencesClass.addUserPassword("")
    }

    private fun getUserDetail(id: String) {
        viewModel.getDetails(id).observe(viewLifecycleOwner, Observer {
            binding.userID.text = it.userID
            binding.userMail.text = it.email
            binding.userStatus.text = it.status
            binding.userName.text = it.userName
        })
    }
}