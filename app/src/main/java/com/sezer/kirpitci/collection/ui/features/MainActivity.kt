package com.sezer.kirpitci.collection.ui.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sezer.kirpitci.collection.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }
}