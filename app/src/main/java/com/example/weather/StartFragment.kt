package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.adapters.CityAdapter
import com.example.weather.databinding.FragmentStartBinding
import com.example.weather.viewmodel.CityInfoViewModel
import com.example.weather.viewmodel.WeatherApiStatus

class StartFragment : Fragment(), CellClickListener{
    private var binding: FragmentStartBinding? = null
    private val sharedViewModel: CityInfoViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    private lateinit var statusObserver : Observer<WeatherApiStatus>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        statusObserver = Observer<WeatherApiStatus>{
            Log.d("statusO", sharedViewModel.status.value.toString())
            if(sharedViewModel.status.value.toString() == "DONE") {
                val selectedCity = binding?.cityName?.text.toString().trim()
                if(selectedCity.isNotEmpty()) {
                    manageSharedPref(selectedCity)
                }
                findNavController().navigate(R.id.action_startFragment_to_infoFragment)
                sharedViewModel.setStatus(WeatherApiStatus.STANDBY)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding?.sendInfo?.setOnClickListener{ getCityDetails() }

        sharedViewModel.status.observe(viewLifecycleOwner, statusObserver)

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
    }

    private fun chooseLayout() {
        when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = CityAdapter(this)
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = CityAdapter(this)
            }
        }
    }

    private fun getCityDetails(){
        val selectedCity = binding?.cityName?.text.toString().trim()
        sharedViewModel.initWeatherData(selectedCity)
    }

    override fun onCellClickListener(data : String) {
        binding?.cityName?.text?.clear()
        sharedViewModel.initWeatherData(data)
    }
}