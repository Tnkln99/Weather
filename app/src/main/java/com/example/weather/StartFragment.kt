package com.example.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.weather.databinding.FragmentStartBinding
import com.example.weather.viewmodel.CityInfoViewModel

class StartFragment : Fragment() {
    private var binding: FragmentStartBinding? = null
    private val sharedViewModel: CityInfoViewModel by activityViewModels()

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
        binding?.startFragment = this
        binding?.sendInfo?.setOnClickListener{ 
            getCityDetails()
        }
    }
    private fun getCityDetails(){
        val selectedCity = binding?.cityName?.text.toString().trim() //burası dogru
        Log.d("getCityDet", selectedCity)
        if(selectedCity.isEmpty()){
            binding?.cityName?.error = "can't be empty"
        }
        else {
            sharedViewModel.initWeatherData(selectedCity)
            findNavController().navigate(R.id.action_startFragment_to_infoFragment)
        }
    }

}