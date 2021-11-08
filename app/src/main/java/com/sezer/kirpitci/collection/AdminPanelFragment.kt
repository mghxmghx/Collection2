package com.sezer.kirpitci.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.sezer.kirpitci.collection.databinding.FragmentAdminPanelBinding
import com.sezer.kirpitci.collection.databinding.FragmentLoginBinding
import com.sezer.kirpitci.collection.ui.features.login.LoginViewModel
import com.sezer.kirpitci.collection.utis.ViewPagerAdapter

class AdminPanelFragment : Fragment() {
    private lateinit var binding: FragmentAdminPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAdminPanelBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //goToAddItem()
        initialTablayout()
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initialTablayout(){
        val viewPagerAdapter=ViewPagerAdapter(requireActivity().supportFragmentManager,2)
        binding.viewPager.apply {
            adapter=viewPagerAdapter

        }
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tablayout))
        binding.tablayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem=tab.position
                Log.d("TAG", "onTabSelected: -----------------"+tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
   /* private fun goToAddItem(){
        binding.AddItemButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminPanelFragment_to_adminAddCardFragment)

        }
    }*/
}