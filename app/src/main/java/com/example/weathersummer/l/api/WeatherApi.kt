package com.example.weathersummer.l.api

import com.example.weathersummer.l.model.City
import com.example.weathersummer.l.model.CurrentWeather
import com.example.weathersummer.l.model.DailyWeather
import com.example.weathersummer.l.model.HoursWeather
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("locations/v1/cities/geoposition/search")
    fun getCity(@Query("apikey") apiKey :String ,
                @Query("q") coordinates :String ,
                @Query("language") language :String,
                @Query("toplevel") topLevel:Boolean
                ) : Deferred<Response<City>>
     @GET("currentconditions/v1/{key}")
    fun getCurrentWeather(@Path ("key") key: String?,
                          @Query("apikey") apiKey:String,
                          @Query("language") language:String,
                          @Query("details") details:Boolean
                          ) :Deferred<List<CurrentWeather>>
    @GET("forecasts/v1/hourly/12hour/{key}")
    fun getHoursWeather( @Path ("key") key: String?,
                          @Query("apikey") apiKey:String,
                          @Query("language") language:String,
                          @Query("metric") metric:Boolean

    ) :Deferred<List<HoursWeather>>
    @GET("forecasts/v1/daily/5day/{key}")
    fun getDailyWeather(@Path ("key") key: String?,
                        @Query("apikey") apiKey:String,
                        @Query("language") language:String,
                        @Query("metric") metric:Boolean) : Deferred<Response<DailyWeather>>

}