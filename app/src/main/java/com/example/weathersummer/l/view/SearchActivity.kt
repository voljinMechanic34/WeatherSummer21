package com.example.weathersummer.l.view

import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathersummer.R
import com.example.weathersummer.WeatherActivity
import com.example.weathersummer.l.db.DataBaseHelper
import com.example.weathersummer.l.model.modelSave.ModelSearchData
import com.example.weathersummer.l.adapters.AdapterSearchCities
import com.example.weathersummer.l.adapters.OnItemClickListener
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Error
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(),OnItemClickListener {

    private var mDBHelper: DataBaseHelper? = null
    private var mDb: SQLiteDatabase? = null
    lateinit var adapter: AdapterSearchCities
    lateinit var help: ArrayList<ModelSearchData>
    val listEmpty: ArrayList<ModelSearchData> = ArrayList()
    var job: Job = Job()
    private val spanHighlight by lazy {
        ForegroundColorSpan(
            ResourcesCompat.getColor(resources, R.color.colorAccent, null)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar_search)
        supportActionBar?.setTitle(getString(R.string.cities))
        toolbar_search.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_search.setNavigationOnClickListener {

            onBackPressed()
        }
        mDBHelper = DataBaseHelper(this)
        val layout = LinearLayoutManager(this)
        val adapter = AdapterSearchCities(this)
        recycler_Search_City.layoutManager = layout
        recycler_Search_City.itemAnimator = DefaultItemAnimator()
        recycler_Search_City.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_Search_City.adapter = adapter
        adapter.setSpanText(spanHighlight)

        openDatabase()

        editText_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var text: String = p0?.trim().toString()
                if (job?.isActive) {
                    job!!.cancel()
                    Log.i("TAG", "job cancel")
                }
                if (text.length > 1) {
                    Log.i("TAG", "OK")


                    job = CoroutineScope(Dispatchers.IO).launch {
                        delay(1200)
                        help = filterItems(text)
                        Log.i("TAG/set", "Setter")
                        withContext(Dispatchers.Main) {
                            adapter.setListItem(help)
                            adapter.setSpanLength(text)
                            adapter.notifyDataSetChanged()
                        }
                    }


                } else {
                    Log.i("TAG", "not OK")
                    adapter.setListItem(listEmpty)
                    adapter.notifyDataSetChanged()
                }


            }

        })
    }

    fun openDatabase() {
        try {
            mDBHelper!!.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        try {
            mDb = mDBHelper!!.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }

        if (mDb!!.isOpen)
            Log.i("TAG", "OPEN")
    }

    fun filterItems(text: String): ArrayList<ModelSearchData> {
        var list2: ArrayList<ModelSearchData> = ArrayList()
        val column = arrayOf("*$text*")
        val c = mDb?.query(
            "geo",
            arrayOf("lat", "lng", "city", "country", "region"),
            "city glob ?",
            column,
            null,
            null, "city ASC"
        )
        if (c?.moveToFirst()!!) {
            do {

                list2?.add(
                    ModelSearchData(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4)
                    )
                )
                // Log.i("TAG",list2.last)

            } while (c.moveToNext())
        }
        c.close()
        return list2

    }

    override fun onClickItem(i: Int) {
        Log.i("TAG", i.toString())
        val model = help.get(i)
        Log.i("TAG", model.lat)
        val intent = Intent(this,WeatherActivity::class.java)
        intent.putExtra("lat", model.lat)
        intent.putExtra("lng", model.lng)
        startActivity(intent)
    }
}
