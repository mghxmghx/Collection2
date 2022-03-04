package com.sezer.kirpitci.collection.ui.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.ActivityUserBinding
import com.sezer.kirpitci.collection.ui.features.user.home.UserFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.beer.BeerFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis.GeneralAnalysisFragment
import com.sezer.kirpitci.collection.ui.features.user.ui.notifications.panel.PersonalFragment


class UserAct : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var chipNavigationBar: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupBottomNav()
    }

    private fun setupBottomNav() {

        chipNavigationBar = findViewById<ChipNavigationBar>(R.id.chipNavigation)
        supportFragmentManager.beginTransaction().replace(R.id.container, UserFragment()).commit()
        chipNavigationBar.setItemSelected(R.id.home_lay, true)
        chipNavigationBar.setOnItemSelectedListener(object :
            ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(i: Int) {
                var fragment = Fragment()
                when (i) {
                    R.id.home_lay -> fragment = UserFragment()
                    R.id.drink_lay -> fragment = BeerFragment()
                    R.id.user_lay -> fragment = PersonalFragment()
                    R.id.phone_lay -> fragment = GeneralAnalysisFragment()
                    else -> fragment = UserFragment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
                    .commit()
            }
        })
    }

}