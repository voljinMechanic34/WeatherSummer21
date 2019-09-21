package com.example.weathersummer.l.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.*
import com.example.weathersummer.R
import com.example.weathersummer.WeatherActivity
import com.example.weathersummer.l.model.modelSave.Geo
import com.example.weathersummer.l.model.modelSave.ModelState
import com.example.weathersummer.l.model.modelSave.Model_SavedCities
import com.example.weathersummer.l.adapters.AdapterSavedCities
import com.example.weathersummer.l.adapters.OnItemClickListenerSave
import com.example.weathersummer.l.adapters.RecyclerItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_saved_search.*
import kotlinx.android.synthetic.main.saved_cities.*

class SavedSearchActivity : AppCompatActivity() , RecyclerItemTouchHelper.RecyclerItemTouchHelperListener ,OnItemClickListenerSave{


    val listItem: ArrayList<Model_SavedCities> = ArrayList()
    var listGeo : ArrayList<Geo> = ArrayList()
    var listState : ArrayList<ModelState> = ArrayList()
    lateinit var  adapter: AdapterSavedCities
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_search)
        setSupportActionBar(toolbar_saved_search)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.cities))
        toolbar_saved_search.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_saved_search.setNavigationOnClickListener {
            saveSp()
            onBackPressed()
        }

        Log.i("TAG","ONCREATE")
        adapter = AdapterSavedCities(this)
        recycler_Saved_City.layoutManager = LinearLayoutManager(this)
        recycler_Saved_City.itemAnimator = DefaultItemAnimator()
        recycler_Saved_City.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recycler_Saved_City.adapter = adapter
        val  itemTouchHelper : ItemTouchHelper.SimpleCallback = RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recycler_Saved_City)
        button_AddCities.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("TAG","Ostart")
        val list = loadSP()
        if (list != null){
            loadData(list)
        } else{
            Log.i("TAG","null")
        }
    }

    override fun onStop() {
        super.onStop()

        listItem.clear()
        Log.i("TAG","PAUSE2")

        val listSave = ArrayList<ModelState>()
        val listGeoS = ArrayList<Geo>()
        //Log.i("TAG/SIZE",list.size.toString())
        for (i in 0..listState.size-1){
            listSave.add(
                ModelState(
                    listState[i].weather,
                    listState[i].city,
                    listState[i].hoursWeather,
                    listState[i].dailyForecasts
                )
            )
            listGeoS.add(listGeo[i])
            //Log.i("TAG/city",list[i].city?.LocalizedName)
        }
        //listSave.add(model)
        val sharedPref = getSharedPreferences("PREF2", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        val gson = Gson()
        val json = gson.toJson(listSave)
        val jsonGeo = gson.toJson(listGeoS)
        editor.putInt("value1",listSave.size)
        editor.putString("value2",json)
        editor.putString("value3",jsonGeo)
        editor.apply()

    }
    fun loadSP() : ArrayList<ModelState>? {
        val sharedPref = getSharedPreferences("PREF2", Context.MODE_PRIVATE)
        if (!sharedPref.contains("value2")){
            Log.i("TAG/Load",sharedPref.contains("value2").toString())
            return null
        }
        val countView = sharedPref.getInt("value1",0)
        Log.i("TAG/count",countView.toString())
        val gson = Gson()
        val textObj = sharedPref.getString("value2",null)
        val type = object : TypeToken<List<ModelState>>(){}.type
        val listNew = gson.fromJson(textObj,type) as ArrayList<ModelState>

        val textObj2 = sharedPref.getString("value3",null)
        val type2 = object : TypeToken<List<Geo>>(){}.type
        val listGeosp = gson.fromJson(textObj2,type2) as ArrayList<Geo>
        listState = listNew
        listGeo = listGeosp
        Log.i("TAG","used sp")
        return listNew
        //Log.i("TAG", obj.text1)
    }
    fun loadData(listSp: ArrayList<ModelState>){
        //val listItem : ArrayList<Model_SavedCities> = ArrayList()

        for (i in 0..listSp.size-1){
           val  text_city = listSp.get(i).city?.LocalizedName
           val text_degree = listSp.get(i).weather.temperatature.metric.value.toString()
           val text_description = listSp.get(i).weather.wetherText
           val  model =
               Model_SavedCities(text_city, text_degree, text_description)
            listItem.add(model)
        }

        adapter.setListItem(listItem)
        adapter.notifyDataSetChanged()
        Log.i("TAG","load from sp")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is AdapterSavedCities.ViewHolder) {
            // get the removed item name to display it in snack bar

            val name  =  listItem.get(viewHolder.adapterPosition).text_city.toString()
            // backup of removed item for undo purpose
            val deletedItem = listItem.get(viewHolder.adapterPosition)
            val deletedIndex = viewHolder.adapterPosition

            val modelState = listState.removeAt(deletedIndex)
            val geoState = listGeo.removeAt(deletedIndex)
            adapter.removeItem(viewHolder.adapterPosition)

            val snackbar = Snackbar.make(coordinator,name,Snackbar.LENGTH_SHORT)
            snackbar.setAction("UNDO", View.OnClickListener {
                adapter.restoreItem(deletedItem,deletedIndex)
                listState.add(deletedIndex,modelState)
                listGeo.add(deletedIndex,geoState)
            })
            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }

    }
    override fun onClickItem(i: Int) {
        saveSp()
        Log.i("TAG", i.toString())
        val intent = Intent(this, WeatherActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("position", i)
        startActivity(intent)
    }
    fun saveSp(){
        listItem.clear()
        Log.i("TAG","PAUSE2")

        val listSave = ArrayList<ModelState>()
        val listGeoS = ArrayList<Geo>()
        //Log.i("TAG/SIZE",list.size.toString())
        for (i in 0..listState.size-1){
            listSave.add(
                ModelState(
                    listState[i].weather,
                    listState[i].city,
                    listState[i].hoursWeather,
                    listState[i].dailyForecasts
                )
            )
            listGeoS.add(listGeo[i])
            //Log.i("TAG/city",list[i].city?.LocalizedName)
        }
        //listSave.add(model)
        val sharedPref = getSharedPreferences("PREF2", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        val gson = Gson()
        val json = gson.toJson(listSave)
        val jsonGeo = gson.toJson(listGeoS)
        editor.putInt("value1",listSave.size)
        editor.putString("value2",json)
        editor.putString("value3",jsonGeo)
        editor.apply()
    }
}

