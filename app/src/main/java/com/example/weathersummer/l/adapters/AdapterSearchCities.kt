package com.example.weathersummer.l.adapters

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathersummer.R
import com.example.weathersummer.l.model.modelSave.ModelSearchData
import kotlinx.android.synthetic.main.item_search_cities.view.*
import kotlin.collections.ArrayList

class AdapterSearchCities (var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdapterSearchCities.ViewHolder>()  {
    var color2 : ForegroundColorSpan? = null
    var textSpan : String = ""
    var list : ArrayList<ModelSearchData> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSearchCities.ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_search_cities,parent,false)
        return AdapterSearchCities.ViewHolder(view,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setSpanText(color: ForegroundColorSpan){
            color2 = color
    }
    override fun onBindViewHolder(holder: AdapterSearchCities.ViewHolder, position: Int) {
        val model = list.get(position)
        val text = "${model.city},${model.region},${model.country}"

        val lengthSpan = text.indexOf(textSpan)
        val text2 = SpannableString(text)

        text2.setSpan(color2,lengthSpan,lengthSpan+textSpan.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.textV.text = text2
    }
    class ViewHolder(itemView : View,val onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val  textV : TextView = itemView.text_search_city
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
                onItemClickListener.onClickItem(adapterPosition)
        }
    }
    fun setListItem(list2 : ArrayList<ModelSearchData>) {
        list = list2
    }

    fun setSpanLength(text: String) {
            textSpan = text
    }

}
interface  OnItemClickListener{
   fun onClickItem(i:Int)
}