package com.example.weathersummer.l.model

import com.google.gson.annotations.SerializedName

data class DailyWeather (@SerializedName("DailyForecasts") val dailyForecasts : List<DailyForecasts>)
data class DailyForecasts (@SerializedName("Temperature") val dailyTemp : TempDaily,
                           @SerializedName("EpochDate") val timeDaily : Long,
                           @SerializedName("Day") val dayDaily : DayDaily)
data class TempDaily (@SerializedName("Minimum") val dailyMininmum : DailyMinimum,
                      @SerializedName("Maximum") val dailyMaximum: DailyMaximum)
data class DailyMinimum (@SerializedName("Value") val dailyValueMin : Double)
data class DailyMaximum (@SerializedName("Value") val dailyValueMax : Double)

data class DayDaily (@SerializedName("Icon") val valueIconDaily : Int)