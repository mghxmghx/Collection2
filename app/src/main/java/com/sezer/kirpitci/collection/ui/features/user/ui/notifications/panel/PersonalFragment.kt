package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.databinding.FragmentPersonalBinding
import com.sezer.kirpitci.collection.utis.adapters.xx
import com.sezer.kirpitci.collection.utis.viewpagers.PersonalViewPagerAdapter
import android.R
import android.graphics.Color

import androidx.core.content.res.TypedArrayUtils.getText

import android.widget.AdapterView
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class PersonalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialTablayout()
        setUpTabLayout()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpTabLayout(){
        val tabLayout = binding.tablayout
        tabLayout.addTab(tabLayout.newTab().setText("Profile Settings"))
        tabLayout.addTab(tabLayout.newTab().setText("Analystics"))
        tabLayout.addTab(tabLayout.newTab().setText("Write Us"))
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"))

    }
    private fun initialTablayout() {
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.position == 0) {
                    binding.fragmentContainerView3.isVisible = false
                    binding.fragmentContainerView4.isVisible = false
                    binding.fragmentContainerView5.isVisible = true

                } else if(tab.position == 1){
                    binding.fragmentContainerView3.isVisible = false
                    binding.fragmentContainerView4.isVisible = true
                    binding.fragmentContainerView5.isVisible = false

                } else if(tab.position == 2){
                    binding.fragmentContainerView3.isVisible = true
                    binding.fragmentContainerView4.isVisible = false
                    binding.fragmentContainerView5.isVisible = false

                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}