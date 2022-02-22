package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentPersonalBinding


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

    private fun setUpTabLayout() {
        val tabLayout = binding.tablayout
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_personal_profile_settings)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_personal_profile_analystics)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fragment_personal_profile_write_us)))
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"))

    }

    private fun initialTablayout() {
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    binding.fragmentContainerView3.isVisible = false
                    binding.fragmentContainerView4.isVisible = false
                    binding.fragmentContainerView5.isVisible = true

                } else if (tab.position == 1) {
                    binding.fragmentContainerView3.isVisible = false
                    binding.fragmentContainerView4.isVisible = true
                    binding.fragmentContainerView5.isVisible = false

                } else if (tab.position == 2) {
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