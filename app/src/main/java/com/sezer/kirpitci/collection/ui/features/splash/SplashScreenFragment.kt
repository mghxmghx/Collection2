package com.sezer.kirpitci.collection.ui.features.splash

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentLoginBinding
import com.sezer.kirpitci.collection.databinding.FragmentSplashScreenBinding
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel
import com.sezer.kirpitci.collection.utis.SharedPreferencesClass
import com.sezer.kirpitci.collection.utis.SplashViewModelFactory
import com.sezer.kirpitci.collection.utis.ViewModelFactory

class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var VM: SplashViewModel
    private lateinit var sharedPreferencesClass: SharedPreferencesClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        innitialVM()
        innitialShared()
        startAnimation()
        Handler().postDelayed(this::auth, 500)
        super.onViewCreated(view, savedInstanceState)
    }
    fun innitialShared(){
        sharedPreferencesClass= SharedPreferencesClass()
        context?.let { sharedPreferencesClass.instantPref(it) }
        Log.d("TAG", "innitialShared: "+sharedPreferencesClass.getEmail()+" "+sharedPreferencesClass.getPassword())
    }
    fun innitialVM(){
        val factory= SplashViewModelFactory()
        VM= ViewModelProvider(this,factory)[SplashViewModel::class.java]
    }
    private fun auth(){
        if (!sharedPreferencesClass.getEmail().toString().isEmpty() && !sharedPreferencesClass.getPassword().toString().isEmpty()){
            VM.auth(sharedPreferencesClass.getEmail().toString(),sharedPreferencesClass.getPassword().toString()).observe(this,
                Observer {
                    if(it){
                            VM.getStatus().observe(viewLifecycleOwner, Observer {
                                if(it.equals("user")){
                                    hideAnimation()
                                    Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_userFragment)
                                }
                                else if(it.equals("admin")){
                                    hideAnimation()
                                    Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_adminPanelFragment)
                                }
                                else
                                {
                                    hideAnimation()
                                    Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_loginFragment)
                                }
                            })
                    }
                    else{
                        hideAnimation()
                        Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_loginFragment)
                    }
                })
        }
        else{
            hideAnimation()
            Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment2_to_loginFragment)
        }
    }
    fun startAnimation(){
        Runnable {
            binding.animationView.isVisible=true

        }

    }
    fun hideAnimation(){
        Runnable {
            binding.animationView.isVisible=false

        }

    }
}