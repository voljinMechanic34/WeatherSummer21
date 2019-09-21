package com.example.weathersummer

import android.util.Log
import com.example.weathersummer.l.BasePresenter

import com.example.weathersummer.l.Constants
import com.example.weathersummer.l.model.modelSave.ModelState
import com.example.weathersummer.l.api.WeatherClient
import kotlinx.coroutines.*

class WeatherPresenter() : BasePresenter {
    var view: WeatherView? = null
   lateinit var job : Job
     fun getWeather(latitude: Double?, longitude: Double?){
         var cityKey: String?
         view?.showLoading()



             job = CoroutineScope(Dispatchers.IO).launch {
             val service = WeatherClient.getApiService()
             val coordinate = "$latitude,$longitude"
             val city =  service.getCity(Constants.API_KEY,coordinate,"ru",true).await()

             cityKey =  city.body()?.key
                 if (cityKey == null)
                 {
                     cityKey = "291310"
                 }
             val currentWeather = service.getCurrentWeather(key = cityKey, apiKey = Constants.API_KEY,
                 language = "ru", details = true).await()
             val hoursWeather = service.getHoursWeather(cityKey,Constants.API_KEY,"en",true).await()
             val dailyWeather = service.getDailyWeather(cityKey,Constants.API_KEY,"en",true).await()
             withContext(Dispatchers.Main)
             {
                /* view?.setCity(city.body())
                 view?.setWeather(currentWeather[0])
                 view?.setHoursWeather(hoursWeather = hoursWeather)
                 view?.setDailyWeather(dailyWeather.body()!!.dailyForecasts.takeLast(4))*/
                 Log.i("TAG/C","USE")
                 view?.setData(
                     ModelState(
                         currentWeather[0],
                         city.body(),
                         hoursWeather,
                         dailyWeather.body()!!.dailyForecasts.takeLast(4)
                     )
                 )

                 view?.hideLoading()
             }
         }





           /* Log.i("TAG", city.body()?.TimeZone?.GmtOffset.toString())
            Log.i("TAG", city.body()?.LocalizedName.toString())
            //Log.i("TAG", currentWeather[0]?.wetherText)
            Log.i("TAG", currentWeather[0].WeatherIcon.toString())*/

    }
    fun onLocationPermissionGranted() {
            view?.updateLocation()
    }
    fun onLocationPermissionDenied() {
            view?.requestPermission()
    }

    fun useCurrentLocation(){
        if (view?.permissionGiven()!!){
            view?.updateLocation()
        } else {
            view?.requestPermission()
        }
    }
    override fun attachView(mvpView: Any?) {
        view = mvpView as WeatherView
    }
    override fun detachView() {
        view = null

        Log.i(Constants.TAG,"View Detach")
    }

    override fun viewIsReady() : Boolean {
        return view != null
    }

}



