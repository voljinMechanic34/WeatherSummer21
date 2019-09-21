package com.example.weathersummer.l.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathersummer.R
import com.example.weathersummer.l.Utils
import com.example.weathersummer.l.model.HoursWeather
import kotlinx.android.synthetic.main.item_hour.view.*
import java.util.*

class AdapterItem(val context: Context) : RecyclerView.Adapter<AdapterItem.ViewHolder>() {
    var itemList: List<HoursWeather> = Collections.emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view : View = LayoutInflater.from(context).inflate(R.layout.item_hour,parent,false)
            return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val hoursWeather : HoursWeather = itemList.get(position)
            holder.textTimeItem.text =
                Utils.getRealTime(hoursWeather.textTimeItem)
            holder.imageItem.setImageResource(Utils.getImageWeather(hoursWeather.imageWeatherItem))
            holder.weatherValueItem.text =
               hoursWeather.temperature2.tempWeatherItem.toString()

    }

    fun setListItem(hoursWeather: List<HoursWeather>) {
        itemList = hoursWeather
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  textTimeItem : TextView  = itemView.textTime
        val  imageItem : ImageView = itemView.imageItem
        val weatherValueItem : TextView = itemView.textItemWeather
    }
}