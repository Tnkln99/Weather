package com.example.weather.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.CellClickListener
import com.example.weather.R


class CityAdapter(private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var list : List<String> = listOf("Your first search will be here")

    class CityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        val context = parent.context
        val sharedPref =
            context.getSharedPreferences("WeatherAppSearchHis",Context.MODE_PRIVATE)
        if(sharedPref.all.values.toList().isNotEmpty()){ list = sharedPref.all.values.toList() as List<String>}

        layout.accessibilityDelegate = Accessibility
        return CityViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = list[position]
        holder.button.text = item
        holder.itemView.setOnClickListener {
            //val selectedCity = holder.button.text.toString()
            cellClickListener.onCellClickListener(item)
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