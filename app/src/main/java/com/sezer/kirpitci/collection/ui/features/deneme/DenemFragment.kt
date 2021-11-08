package com.sezer.kirpitci.collection.ui.features.deneme

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sezer.kirpitci.collection.R
import com.sezer.kirpitci.collection.databinding.FragmentDenemBinding
import com.sezer.kirpitci.collection.databinding.FragmentLoginBinding

class DenemFragment : Fragment() {
    private lateinit var binding2: FragmentDenemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding2= FragmentDenemBinding.inflate(inflater,container,false)
        return binding2.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        xxx()
        super.onViewCreated(view, savedInstanceState)
    }



    private fun xxx(){

        binding2.button3.setOnClickListener{
            var eee:String="sssss"

            eee=binding2.textView2.text.toString()
            Log.d("TAG", "xxx:sdasdasdasd ")
            Toast.makeText(context,eee, Toast.LENGTH_SHORT).show()
        }


    }

}