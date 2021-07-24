package com.example.weather

import android.content.Context
import android.os.Bundle
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

    private val sharedPref = context?.getSharedPreferences("city names",Context.MODE_PRIVATE)

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

        recyclerView = binding!!.recyclerView
        chooseLayout()
        binding?.viewModel = sharedViewModel
        binding?.startFragment = this //burası sıkıntı cıkarıyo apply ile yaparsam
        binding?.sendInfo?.setOnClickListener{
            getCityDetails()
        }
    }
    private fun getCityDetails(){
        val selectedCity = binding?.cityName?.text.toString().trim()
        manageSharedPref(selectedCity)
        //Log.d("getCityDet", selectedCity)
        if(sharedViewModel.initWeatherData(selectedCity)){
            //Log.d("inStartFrag",sharedViewModel.status.value.toString())
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
        var id : Int? = sharedPref?.all?.size
        if (id != null) {
            id += 1
        }
        if (sharedPref != null) { //bu null check gerekli mi emin değilim
            with (sharedPref.edit()) {
                putString(id.toString(), selectedCity)
                apply()
            }
        }
    }

}