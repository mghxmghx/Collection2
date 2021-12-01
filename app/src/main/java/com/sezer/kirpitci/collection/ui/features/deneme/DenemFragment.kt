package com.sezer.kirpitci.collection.ui.features.deneme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sezer.kirpitci.collection.databinding.FragmentDenemBinding

class DenemFragment : Fragment() {
    private lateinit var binding2: FragmentDenemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding2 = FragmentDenemBinding.inflate(inflater, container, false)
        return binding2.root
    }


}

