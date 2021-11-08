package com.sezer.kirpitci.collection.utis

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sezer.kirpitci.collection.ui.features.admin.addcard.AdminAddCardFragment
import com.sezer.kirpitci.collection.ui.features.admin.viewcard.AdminViewCardFragment

class ViewPagerAdapter(val fragmentManager: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fragmentManager){
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = Fragment()
        if(position==0){
            fragment = AdminAddCardFragment()
        }
        else if(position == 1){
            fragment = AdminViewCardFragment()
        }
        return fragment
    }
}