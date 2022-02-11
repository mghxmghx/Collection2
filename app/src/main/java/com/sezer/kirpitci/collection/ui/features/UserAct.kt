package com.sezer.kirpitci.collection.ui.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sezer.kirpitci.collection.databinding.ActivityUserBinding
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.utis.viewpagers.GeneralViewPagerAdapter


class UserAct : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navView: BottomNavigationView = binding.navView

       // val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_settings
            )
        )
        supportActionBar?.hide()
        //setupActionBarWithNavController(navController, appBarConfiguration)
       // navView.setupWithNavController(navController)
        setupBottomNav()
    }
    private fun setupBottomNav() {
        val drink_lay=binding.root.findViewById<LinearLayout>(R.id.drink_lay);
        val home_lay=binding.root.findViewById<LinearLayout>(R.id.home_lay);
        val user_lay=binding.root.findViewById<LinearLayout>(R.id.user_lay);
        val call_lay=binding.root.findViewById<LinearLayout>(R.id.phone_lay);
        val drink_lay_in=binding.root.findViewById<LinearLayout>(R.id.drink_lay_in);
        val home_lay_in=binding.root.findViewById<LinearLayout>(R.id.home_lay_in);
        val user_lay_in=binding.root.findViewById<LinearLayout>(R.id.user_lay_in);
        val phone_lay_in=binding.root.findViewById<LinearLayout>(R.id.phone_lay_in);

        val home_ic=binding.root.findViewById<ImageView>(R.id.home_ic);
        val drink_ic=binding.root.findViewById<ImageView>(R.id.drink_ic);
        val user_ic = binding.root.findViewById<ImageView>(R.id.user_ic)
        val phone_ic = binding.root.findViewById<ImageView>(R.id.phone_ic)
        val adapterx = GeneralViewPagerAdapter(supportFragmentManager, 4)
        val viewPager = binding.root.findViewById<ViewPager>(R.id.pagerName)
        viewPager.apply {
            adapter = adapterx
        }
        home_lay.setOnClickListener {
            viewPager.currentItem = 0
            home_lay_in.setVisibility(View.VISIBLE);
            drink_ic.setVisibility(View.VISIBLE);
            user_ic.visibility = View.VISIBLE
            phone_ic.visibility = View.VISIBLE

            home_ic.setVisibility(View.GONE);
            drink_lay_in.setVisibility(View.GONE);
            phone_lay_in.visibility = View.GONE
            user_lay_in.visibility = View.GONE
        }

        drink_lay.setOnClickListener {
            viewPager.currentItem = 1
            home_lay_in.setVisibility(View.GONE);
            drink_ic.setVisibility(View.GONE);
            user_ic.visibility = View.VISIBLE
            phone_ic.visibility = View.VISIBLE
            home_ic.setVisibility(View.VISIBLE);
            drink_lay_in.setVisibility(View.VISIBLE);
            phone_lay_in.visibility = View.GONE
            user_lay_in.visibility = View.GONE
        }

        user_lay.setOnClickListener {
            viewPager.currentItem = 2
            home_lay_in.setVisibility(View.GONE);
            drink_ic.setVisibility(View.VISIBLE);
            user_ic.visibility = View.GONE
            phone_ic.visibility = View.VISIBLE

            home_ic.setVisibility(View.VISIBLE);
            drink_lay_in.setVisibility(View.GONE);
            phone_lay_in.visibility = View.GONE
            user_lay_in.visibility = View.VISIBLE
        }

        call_lay.setOnClickListener {
            viewPager.currentItem = 3
            home_lay_in.setVisibility(View.GONE);
            drink_ic.setVisibility(View.VISIBLE);
            user_ic.visibility = View.VISIBLE
            phone_ic.visibility = View.GONE

            home_ic.setVisibility(View.VISIBLE);
            drink_lay_in.setVisibility(View.GONE);
            phone_lay_in.visibility = View.VISIBLE
            user_lay_in.visibility = View.GONE
        }

    }

}