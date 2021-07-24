package com.example.weather.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R


class CityAdapter :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private val list = ('A').rangeTo('Z').toList()

    class CityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        layout.accessibilityDelegate = Accessibility
        return CityViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = list.get(position)
        holder.button.text = item.toString()
        holder.button.setOnClickListener {
            //val selectedCity = holder.button.text.toString()
            holder.view.findNavController().navigate(R.id.action_startFragment_to_infoFragment)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View?,
            info: AccessibilityNodeInfo?
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            val customString = host?.context?.getString(R.string.look_up_cities)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }

}