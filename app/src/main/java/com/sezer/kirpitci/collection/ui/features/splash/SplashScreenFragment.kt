package com.sezer.kirpitci.collection.ui.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentSplashScreenBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.UserAct
import com.sezer.kirpitci.collection.utis.others.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class SplashScreenFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var VM: SplashViewModel
    private lateinit var sharedPreferencesClass: SharedPreferencesClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        innitialVM()
        innitialShared()
        startAnimation()
        Handler().postDelayed(this::auth, 500)
        super.onViewCreated(view, savedInstanceState)
    }

    fun innitialShared() {
        sharedPreferencesClass = SharedPreferencesClass()
        context?.let { sharedPreferencesClass.instantPref(it) }
        Log.d(
            "TAG",
            "innitialShared: " + sharedPreferencesClass.getEmail() + " " + sharedPreferencesClass.getPassword()
        )
    }
    private fun initialUI(){
        MyApp.appComponent.inject(this)
    }
    fun innitialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }

    private fun auth() {
        if (!sharedPreferencesClass.getEmail().toString()
                .isEmpty() && !sharedPreferencesClass.getPassword().toString().isEmpty()
        ) {
            VM.auth(
                sharedPreferencesClass.getEmail().toString(),
                sharedPreferencesClass.getPassword().toString()
            ).observe(this,
                Observer {
                    if (it) {
                        VM.getStatus().observe(viewLifecycleOwner, Observer {
                            if (it.equals("user")) {
                                hideAnimation()
                                val intent = Intent(activity, UserAct::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                                // Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_homePageFragment)
                            } else if (it.equals("admin")) {
                                hideAnimation()
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_splashScreenFragment2_to_adminPanelFragment)
                            } else {
                                hideAnimation()
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_splashScreenFragment2_to_loginFragment)
                            }
                        })
                    } else {
                        hideAnimation()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_splashScreenFragment2_to_loginFragment)
                    }
                })
        } else {
            hideAnimation()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_splashScreenFragment2_to_loginFragment)
        }
    }

    fun startAnimation() {
        Runnable {
            binding.animationView.isVisible = true

        }

    }

    fun hideAnimation() {
        Runnable {
            binding.animationView.isVisible = false

        }

    }
}