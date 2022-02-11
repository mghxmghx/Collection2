package com.sezer.kirpitci.collection.utis.viewpagers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.BeerFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis.GeneralAnalysisFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.home.HomePageFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel.PersonalFragment

class GeneralViewPagerAdapter(val fragmentManager: FragmentManager, internal var totalTabs: Int) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()
        if (position == 0) {
            fragment = HomePageFragment()
        } else if (position == 1) {
            fragment = BeerFragment()
        } else if (position == 2) {
            fragment = PersonalFragment()
        } else if (position == 3) {
            fragment = GeneralAnalysisFragment()
        }
        return fragment
    }
}