package com.sezer.kirpitci.collection.utis.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics.PersonalAnalytics
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel.PersonalFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo.PersonalInfo
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.SettingsFragment


class xx(context: Context, fm: FragmentManager, totalTabs: Int) :
    FragmentStatePagerAdapter(fm) {
    private val myContext: Context
    var totalTabs: Int

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        if(position == 0 ){
            return PersonalInfo()
        } else if(position == 1) {
            return PersonalAnalytics()
        } else if(position == 2){
            return SettingsFragment()
        }
        return Fragment()
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

    companion object {
        private const val TAG = "Page Adapter"
    }

    init {
        myContext = context
        this.totalTabs = totalTabs
    }
}