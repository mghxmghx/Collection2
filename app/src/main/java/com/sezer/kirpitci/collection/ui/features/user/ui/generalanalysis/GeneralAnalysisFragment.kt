package com.sezer.kirpitci.collection.ui.features.user.ui.generalanalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sezer.kirpitci.collection.databinding.FragmentGeneralAnalysisBinding
import com.sezer.kirpitci.collection.di.MyApp
import com.sezer.kirpitci.collection.ui.features.registration.CardModel
import com.sezer.kirpitci.collection.utis.adapters.ClickItemUser
import com.sezer.kirpitci.collection.utis.adapters.GeneralAnalysisAdapter
import com.sezer.kirpitci.collection.utis.others.ViewModelFactory
import javax.inject.Inject

class GeneralAnalysisFragment : Fragment(), ClickItemUser {
    private lateinit var binding: FragmentGeneralAnalysisBinding
    private lateinit var VM: GeneralAnalysisViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var cheapAdapter: GeneralAnalysisAdapter
    private lateinit var expensiveAdapter: GeneralAnalysisAdapter
    private lateinit var teriedAdapter: GeneralAnalysisAdapter
    private lateinit var ratedAdapter: GeneralAnalysisAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGeneralAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialUI()
        initialVM()
        getData()
        initialCheapRecycler()
        initialExpensiveRecycler()
        initialRatedRecycler()
        initialTriedRecycler()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialUI() {
        MyApp.appComponent.inject(this)
    }

    private fun initialVM() {
        VM = ViewModelProvider(this, viewModelFactory)[GeneralAnalysisViewModel::class.java]

    }

    private fun getData() {
        VM.getBeers().observe(viewLifecycleOwner, Observer {
            setAdapters(it)
        })
    }

    private fun setAdapters(list: List<GeneralAnalysisModel>) {
        var cheapList = list.sortedBy {
            it.price
        }
        var expensiveList = list.sortedByDescending {
            it.price
        }
        var voteCountList = list.sortedByDescending {
            it.voteCount
        }
        var averageList = list.sortedByDescending {
            it.average
        }
        if (expensiveList.size > 3) {
            expensiveList = listOf(expensiveList.get(0), expensiveList.get(1), expensiveList.get(2))

        }
        if (voteCountList.size > 3) {
            voteCountList = listOf(voteCountList.get(0), voteCountList.get(1), voteCountList.get(2))

        }
        if (cheapList.size > 3) {
            cheapList = listOf(cheapList.get(0), cheapList.get(1), cheapList.get(2))

        }
        if (averageList.size > 3) {
            averageList = listOf(averageList.get(0), averageList.get(1), averageList.get(2))
        }
        ratedAdapter.submitList(averageList)
        teriedAdapter.submitList(voteCountList)
        expensiveAdapter.submitList(expensiveList)
        cheapAdapter.submitList(cheapList.toMutableList())
    }

    private fun initialCheapRecycler() {
        cheapAdapter = GeneralAnalysisAdapter(3)
        binding.mostCheapecycler.layoutManager = GridLayoutManager(context, 3)
        binding.mostCheapecycler.adapter = cheapAdapter
    }

    private fun initialExpensiveRecycler() {
        expensiveAdapter = GeneralAnalysisAdapter(3)
        binding.mostExpensiveRecycler.layoutManager = GridLayoutManager(context, 3)
        binding.mostExpensiveRecycler.adapter = expensiveAdapter
    }

    private fun initialTriedRecycler() {
        teriedAdapter = GeneralAnalysisAdapter(2)
        binding.mostTriedRecycler.layoutManager = GridLayoutManager(context, 3)
        binding.mostTriedRecycler.adapter = teriedAdapter
    }

    private fun initialRatedRecycler() {
        ratedAdapter = GeneralAnalysisAdapter(0)
        binding.topBeersRecycler.layoutManager = GridLayoutManager(context, 3)
        binding.topBeersRecycler.adapter = ratedAdapter
    }

    override fun clicked(model: CardModel) {
    }
}