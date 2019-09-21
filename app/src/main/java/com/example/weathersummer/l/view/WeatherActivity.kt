package com.example.weathersummer
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.weathersummer.l.Constants
import com.example.weathersummer.l.model.modelSave.Geo
import com.example.weathersummer.l.model.modelSave.ModelState
import com.example.weathersummer.l.Utils
import com.example.weathersummer.l.view.SavedSearchActivity
import com.example.weathersummer.l.adapters.AdapterPage
import com.example.weathersummer.l.model.City
import com.example.weathersummer.l.model.CurrentWeather
import com.example.weathersummer.l.model.DailyForecasts
import com.example.weathersummer.l.model.HoursWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : AppCompatActivity() , WeatherView, LocationListener, SwipeRefreshLayout.OnRefreshListener{

    lateinit var locationManager : LocationManager
    var listWeather : ArrayList<CurrentWeather> = ArrayList()
    var listCity : ArrayList<City> = ArrayList()
    var listHoursWeather : ArrayList<List<HoursWeather>> = ArrayList()
    var listDailyForecats : ArrayList<List<DailyForecasts>> = ArrayList()
    var list = ArrayList<ModelState>()
    var listGeo = ArrayList<Geo>()
    lateinit var viewPager : ViewPager
    lateinit var pagerAdapter : AdapterPage
    var isRefresh : Boolean = false
    var isPickedPlace : Boolean = false
    var isPicked : Boolean = false
    var isLoadDb : Boolean = false
    var views = ArrayList<View>()
    lateinit var presenter: WeatherPresenter
    lateinit var inflater : LayoutInflater
    var position : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)

        swipeRefreshLayout.setOnRefreshListener(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        pagerAdapter = AdapterPage()
        viewPager = view_Pager as ViewPager
        viewPager.adapter = pagerAdapter
        inflater = this.layoutInflater
        viewPager.addOnPageChangeListener(onPageListener)
        /*recyclerView_Hours.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        adapterItem = AdapterItem(this)
        recyclerView_Hours.adapter = adapterItem
        recyclerView_Daily.layoutManager = LinearLayoutManager(this)
        adapterItemDaily = AdapterDaily(this)
        recyclerView_Daily.adapter = adapterItemDaily*/
        Log.i("TAG","Oncreate")
        imageTool.setOnClickListener(View.OnClickListener {
            saveSp()
            val intent = Intent(this, SavedSearchActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        })
        presenter = WeatherPresenter()
        presenter.attachView(this)
        presenter.viewIsReady()



        image_local_place.setOnClickListener{
            isPickedPlace = true
            isPicked = true
            presenter.useCurrentLocation()

        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("TAG","start")
        var listNew = loadSP()
        if(listNew == null){
            presenter.useCurrentLocation()
            Log.i("TAG","INET")
            //loadData()
        } else if (listNew.size>0){
           // isLoadDb = true
            loadDataSp(listNew)
            Log.i("TAG","SP")
        } else if (listNew.size == 0 ){
            linear_layout.setBackgroundColor(Color.BLUE)
            areaCity.text = ""
        }

        val intent = intent
        if(intent.hasExtra("lat")){
            val lat = intent.getStringExtra("lat").toDouble()
            val lng = intent.getStringExtra("lng").toDouble()
            presenter.getWeather(lat,lng)
            listGeo.add(Geo(lat, lng))
        }
    }
       override fun showLoading() {

        /*progressBar.visibility = View.VISIBLE
        areaCity.visibility = View.INVISIBLE
        imageWeather.visibility = View.INVISIBLE
        tempCity.visibility = View.INVISIBLE
        weatherText.visibility = View.INVISIBLE
        tempCityMax.visibility = View.INVISIBLE
        viewLine.visibility = View.INVISIBLE
        viewLine2.visibility = View.INVISIBLE
        viewLine3.visibility = View.INVISIBLE
        viewLine4.visibility = View.INVISIBLE
        recyclerView_Hours.visibility = View.INVISIBLE
        recyclerView_Daily.visibility = View.INVISIBLE
        textComfort.visibility = View.INVISIBLE
        textHumidity.visibility = View.INVISIBLE
        circleProgressBar.visibility = View.INVISIBLE
        textFeelTemp.visibility = View.INVISIBLE
        textFeelTempValue.visibility = View.INVISIBLE
        textUf.visibility = View.INVISIBLE
        textUfValue.visibility = View.INVISIBLE
        textWind.visibility = View.INVISIBLE
        imageWind.visibility = View.INVISIBLE
        textDirection.visibility = View.INVISIBLE
        textDirectionValue.visibility = View.INVISIBLE
        textSpeed.visibility = View.INVISIBLE
        textSpeedValue.visibility = View.INVISIBLE
        imageTool.visibility = View.INVISIBLE*/
    }

    override fun hideLoading() {
        /*progressBar.visibility = View.INVISIBLE
        areaCity.visibility = View.VISIBLE
        imageWeather.visibility = View.VISIBLE
        tempCity.visibility = View.VISIBLE
        weatherText.visibility = View.VISIBLE
        tempCityMax.visibility = View.VISIBLE
        viewLine.visibility = View.VISIBLE
        viewLine2.visibility = View.VISIBLE
        viewLine3.visibility = View.VISIBLE
        viewLine4.visibility = View.VISIBLE
        recyclerView_Hours.visibility = View.VISIBLE
        recyclerView_Daily.visibility = View.VISIBLE
        textComfort.visibility = View.VISIBLE
        textHumidity.visibility = View.VISIBLE
        circleProgressBar.visibility = View.VISIBLE
        textFeelTemp.visibility = View.VISIBLE
        textFeelTempValue.visibility = View.VISIBLE
        textUf.visibility = View.VISIBLE
        textUfValue.visibility = View.VISIBLE
        textWind.visibility = View.VISIBLE
        imageWind.visibility = View.VISIBLE
        textDirection.visibility = View.VISIBLE
        textDirectionValue.visibility = View.VISIBLE
        textSpeed.visibility = View.VISIBLE
        textSpeedValue.visibility = View.VISIBLE
        imageTool.visibility = View.VISIBLE*/
    }

    override fun setData(model: ModelState) {
        //list.add(model)
        if (isPickedPlace){
            position = viewPager.currentItem
            isPickedPlace = false
            refreshPage(model)
            list[position] = model
            Log.i("TAG/s", "picked")
        } else {
            if (!isRefresh) {

                list.add(model)
                Log.i("TAG/s", list.size.toString())
                Log.i("TAG/s", "not picked")
                views.add(inflater.inflate(R.layout.item_page, null))
                pagerAdapter.addView(views.get(list.size - 1))
                Log.i("TAG", list.size.toString())
                pagerAdapter.setList(list)
                pagerAdapter.notifyDataSetChanged()
                setCurrentPage(views.get(list.size - 1))
                if (list.size == 1)
                    onPageListener.onPageSelected(0)

            } else {
                isRefresh = false
                refreshPage(model)
                list[position] = model
            }
        }
    }

    override fun onErrorLoading(message: String) {
    }



    override fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Constants.REQUEST_CODE_LOCATION_PERMISSION
        )
    }

    override fun permissionGiven() : Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSION){
                if (grantResults.size>0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                       presenter.onLocationPermissionGranted()
                } else {
                    presenter.onLocationPermissionDenied()
                }
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onLocationChanged(location: Location?) {

        presenter.getWeather(location?.latitude, location?.longitude)
        if (isPicked){
            isPicked = false
            listGeo[viewPager.currentItem] =
                Geo(location?.latitude, location?.longitude)
        } else {
            listGeo.add(Geo(location?.latitude, location?.longitude))
        }


    }
    override fun updateLocation() {
        // Получаем LocationManager
       //locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        // Получаем лучший провайдер
        /*val criteria = Criteria()
        val bestProvider = locationManager.getBestProvider(criteria, true)*/
        val bestProvider: String = LocationManager.GPS_PROVIDER
        val statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (statusOfGPS){

            Log.i("TAG","WORK")
        }  else {
            Log.i("TAG"," NOT WORK")
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // Получаем последнюю доступную позицию
        val lastKnownLocation = locationManager.getLastKnownLocation(bestProvider!!)
        if (lastKnownLocation!= null)
        {
            //lastKnownLocation.
            Log.v(Constants.TAG, "Last location: " + lastKnownLocation?.toString())
           // presenter.getWeather(lastKnownLocation.latitude, lastKnownLocation.longitude)
        } else{
            Log.v(Constants.TAG, "null")
        }


// Подписываемся на обновления
        locationManager.requestLocationUpdates(
            bestProvider, // провайдер
            0, // мин. время
            1000f, // мин. расстояние
            this
        )
    }

    override fun onPause() {
        super.onPause()


    }

    override fun onStop() {
        super.onStop()
        Log.i("TAG","STOP")

       if(locationManager != null)
            locationManager.removeUpdates(this)
        //val model  = ModelState(listWeather.get(0),listCity.get(0),listHoursWeather.get(0),listDailyForecats.get(0))
        val listSave = ArrayList<ModelState>()
        val listGeoS = ArrayList<Geo>()
        Log.i("TAG/count2",list.size.toString())
        Log.i("TAG/count2",listGeo.size.toString())

        for (i in 0..list.size-1){
            listSave.add(
                ModelState(
                    list[i].weather,
                    list[i].city,
                    list[i].hoursWeather,
                    list[i].dailyForecasts
                )
            )
            listGeoS.add(listGeo[i])

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
        views.clear()
        list.clear()
        pagerAdapter.removeViews(viewPager)
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        viewPager.removeOnPageChangeListener(onPageListener)
        Log.i("TAG","Destroy")

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
        val listGeos = gson.fromJson(textObj2,type2) as ArrayList<Geo>
        if (listGeo != null)
            listGeo = listGeos
        list = listNew

        Log.i("TAG/count",list.size.toString())
        Log.i("TAG/count",listGeo.size.toString())
        return listNew
        //Log.i("TAG", obj.text1)
    }
    fun loadDataSp(listSp: ArrayList<ModelState>){

        for (i in 0..listSp.size-1){
           /* setWeather(listSp[i].weather)
            setCity(listSp[i].city)
            setHoursWeather(listSp[i].hoursWeather)
            setDailyWeather(listSp[i].dailyForecasts)*/
            views.add(inflater.inflate(R.layout.item_page,null))
            pagerAdapter.addView(views.get(i))

        }
        pagerAdapter.setList(listSp)
        pagerAdapter.notifyDataSetChanged()
        onPageListener.onPageSelected(0)

        val intent = intent
        if (intent.hasExtra("position"))
        {   Log.i("TAG","setPos")
            val pos = intent.getIntExtra("position",0)
            setCurrentPage(views[pos])
        }else{
            setCurrentPage(views[0])
        }

        Log.i("TAG/size",listSp.size.toString())
        Log.i("TAG/size",views.size.toString())
       // Log.i("TAG/size",pagerAdapter.ret().toString())
        Log.i("TAG","load from sp")
    }
    fun setCurrentPage(pageToShow: View) {
        viewPager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true)
    }
    val onPageListener = object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            Log.i("TAG",position.toString())
            areaCity.text = list[position].city?.LocalizedName
            swipeRefreshLayout.setBackgroundResource(Utils.getBackgroundForActivity(list[position].weather?.isDayTime))
        }

    }
    override fun onRefresh() {

        position = viewPager.currentItem
        if (list.size == 0){
            updateLocation()
        } else {
            val lat = listGeo[position].lat
            val lng = listGeo[position].lng

            isRefresh = true
            presenter.getWeather(lat,lng)

        }


        swipeRefreshLayout.isRefreshing = false
    }
    fun refreshPage(model: ModelState) {

        pagerAdapter.setItem(model,position)
        val view = viewPager.findViewWithTag<View>(position)
        Log.i("TAG/POS",position.toString())
        val tempCityP = model.weather?.temperatature?.metric?.value.toString()+"°C"
        //view.tempCity.text = tempCityP
        //view.weatherText.text = model.weather?.wetherText
        pagerAdapter.getWeather(model.weather,view)
        pagerAdapter.initRecycler(view)
        pagerAdapter.getHoursWeather(model.hoursWeather)
        pagerAdapter.getDailyWeather(model.dailyForecasts)
        onPageListener.onPageSelected(position)

    }
        fun saveSp(){
            val listSave = ArrayList<ModelState>()
            val listGeoS = ArrayList<Geo>()
            Log.i("TAG/count2",list.size.toString())
            Log.i("TAG/count2",listGeo.size.toString())

            for (i in 0..list.size-1){
                listSave.add(
                    ModelState(
                        list[i].weather,
                        list[i].city,
                        list[i].hoursWeather,
                        list[i].dailyForecasts
                    )
                )
                listGeoS.add(listGeo[i])

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
            views.clear()
            list.clear()
            pagerAdapter.removeViews(viewPager)
        }
}