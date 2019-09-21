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
import com.example.weathersummer.l.model.DailyForecasts
import kotlinx.android.synthetic.main.item_daily.view.*
import java.util.*

class AdapterDaily(val context: Context) : RecyclerView.Adapter<AdapterDaily.ViewHolder>() {
    var itemList: List<DailyForecasts> = Collections.emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item_daily,parent,false)
        return AdapterDaily.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyWeather : DailyForecasts = itemList.get(position)
        holder.textTimeItemDaily.text =
            Utils.getRealTimeDaily(dailyWeather.timeDaily)
        holder.imageItemDaily.setImageResource(Utils.getImageWeather(dailyWeather.dayDaily.valueIconDaily))

        val txt = dailyWeather.dailyTemp.dailyMaximum.dailyValueMax.toString()+"°/"+
                dailyWeather.dailyTemp.dailyMininmum.dailyValueMin.toString()
        holder.weatherValueItemDaily.text = txt

    }
    fun setListItem(dailyList: List<DailyForecasts>) {
        itemList = dailyList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  textTimeItemDaily : TextView = itemView.textTimeDaily
        val  imageItemDaily : ImageView = itemView.imageItemDaily
        val weatherValueItemDaily : TextView = itemView.textItemWeatherDaily
    }
}