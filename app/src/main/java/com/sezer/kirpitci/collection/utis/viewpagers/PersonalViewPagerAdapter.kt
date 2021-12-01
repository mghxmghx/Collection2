package com.sezer.kirpitci.collection.utis.viewpagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.analytics.PersonalAnalytics
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.personalinfo.PersonalInfo
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.settings.SettingsFragment

class PersonalViewPagerAdapter(val fragmentManager: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = Fragment()
        if (position == 0) {
            fragment = PersonalInfo()
        } else if (position == 1) {
            fragment = PersonalAnalytics()
        } else if (position == 2) {
            fragment = SettingsFragment()
        }
        return fragment
    }
}