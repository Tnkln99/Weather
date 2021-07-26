package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.adapters.CityAdapter
import com.example.weather.databinding.FragmentStartBinding
import com.example.weather.viewmodel.CityInfoViewModel

class StartFragment : Fragment() {
    private var binding: FragmentStartBinding? = null
    private val sharedViewModel: CityInfoViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding?.recyclerView ?: return
        chooseLayout()

        binding?.viewModel = sharedViewModel
        binding?.startFragment = this
        binding?.sendInfo?.setOnClickListener{
            getCityDetails()
        }
    }
    private fun getCityDetails(){
        val selectedCity = binding?.cityName?.text.toString().trim()
        //Log.d("getCityDet", selectedCity)
        if(sharedViewModel.initWeatherData(selectedCity)){
            manageSharedPref(selectedCity)
            findNavController().navigate(R.id.action_startFragment_to_infoFragment)
        }
        else { binding?.cityName?.error = "something went wrong! Check city name or your connection" }
    }

    private fun chooseLayout() {
        when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = CityAdapter()
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = CityAdapter()
            }
        }
    }

    private fun manageSharedPref(selectedCity : String){
        val sharedPref = activity?.getSharedPreferences(
            "WeatherAppSearchHis", Context.MODE_PRIVATE)?: return
        var id = sharedPref.all.size
        id += 1
        with (sharedPref.edit()) {
            putString(id.toString(), selectedCity)
            apply()
        }
        Log.d("startFrag", sharedPref.all.values.toList().toString())
    }

}