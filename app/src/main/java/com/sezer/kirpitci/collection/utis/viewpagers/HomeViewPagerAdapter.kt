package com.sezer.kirpitci.collection.utis.viewpagers

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeViewPagerAdapter(val fragmentManager: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()
        Log.d("TAG", "getItem: " + position)
        if (position == 0) {
            //  return PersonalInfo()
        } else if (position == 1) {
            //     return PersonalAnalytics()
        } else if (position == 2) {
            //      return SettingsFragment()
        }
        return fragment
    }
}