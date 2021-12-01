package com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.databinding.FragmentPersonalBinding
import com.sezer.kirpitci.collection.utis.viewpagers.PersonalViewPagerAdapter


class PersonalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalBinding
    private lateinit var viewPagerAdapter: PersonalViewPagerAdapter
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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialTablayout() {
        val viewPagerAdapter = PersonalViewPagerAdapter(requireActivity().supportFragmentManager, 3)
        binding.viewpager.apply {
            adapter = viewPagerAdapter

        }
        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tablayout))
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}