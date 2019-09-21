package com.example.weathersummer.l.model

import com.google.gson.annotations.SerializedName

data class HoursWeather (@SerializedName("EpochDateTime") val textTimeItem : Long,
                         @SerializedName("WeatherIcon") val imageWeatherItem : Int,
                         @SerializedName("Temperature") val temperature2 : Temperature2)

data class Temperature2 (@SerializedName("Value") val tempWeatherItem : Double)
