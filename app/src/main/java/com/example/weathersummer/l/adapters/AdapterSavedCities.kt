package com.example.weathersummer.l.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathersummer.R
import com.example.weathersummer.l.model.modelSave.Model_SavedCities
import kotlinx.android.synthetic.main.item_saved_cities.view.*
import kotlin.collections.ArrayList

class AdapterSavedCities(var onItemClickListener: OnItemClickListenerSave) : RecyclerView.Adapter<AdapterSavedCities.ViewHolder>()  {
    var itemList: ArrayList<Model_SavedCities> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_cities,parent,false)
        return ViewHolder(view,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model_SavedCities : Model_SavedCities =  itemList.get(position)
        holder.text_city.text = model_SavedCities.text_city
        holder.text_degree.text = model_SavedCities.text_degree
        holder.text_description.text = model_SavedCities.text_description
    }
    fun setListItem(savedCities: ArrayList<Model_SavedCities>) {
        itemList = savedCities
    }
    fun removeItem(position: Int){
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
    fun restoreItem(model : Model_SavedCities, position: Int){
            itemList.add(position,model)
            notifyItemInserted(position)
    }
    class ViewHolder (itemView: View,val onItemClickListener: OnItemClickListenerSave) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val  text_city : TextView = itemView.text_savedCities_city
        val  text_degree : TextView = itemView.text_savedCities_degree
        val  text_description: TextView = itemView.text_savedCities_description
        val viewBackground : RelativeLayout = itemView.view_background
        val viewForeground : RelativeLayout = itemView.view_foreground
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClickListener.onClickItem(adapterPosition)
        }
    }
}
interface  OnItemClickListenerSave{
    fun onClickItem(i:Int)
}