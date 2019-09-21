package com.example.weathersummer.l.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather (@SerializedName("WeatherText") val wetherText : String?,
                           val WeatherIcon : Int,
                           @SerializedName("Temperature") val temperatature : Temperature,
                           @SerializedName("TemperatureSummary") val tempSummary : TemperatureSummary,

                           @SerializedName("RelativeHumidity") val humidity : Int?,
                           @SerializedName("Wind") val wind : Wind,
                           @SerializedName("RealFeelTemperature") val realFeelTemp : RealFeelTemperature,
                           @SerializedName("IsDayTime") val isDayTime : Boolean,
                           @SerializedName("UVIndex") val uvIndex : Int,
                           @SerializedName("UVIndexText") val uvIndexText : String





                           )
data class Temperature (@SerializedName("Metric") val metric : Metric)
data class Metric (@SerializedName("Value") val value : Double)

data class TemperatureSummary (@SerializedName("Past24HourRange") val pastDayTemperature : PastDayTemperature)
data class PastDayTemperature (@SerializedName("Minimum") val minPastDayTemperature : Minimum,
                               @SerializedName("Maximum") val maxPastDayTemperature : Maximum)
data class Minimum (@SerializedName("Metric") val metricMinPastDayTemperature : MetricMinPastDayTemperature)
data class MetricMinPastDayTemperature (@SerializedName("Value") val valueMinPastDayTemperature : Double)


data class Maximum (@SerializedName("Metric") val metricMaxPastDayTemperature : MetricMaxPastDayTemperature)
data class MetricMaxPastDayTemperature (@SerializedName("Value") val valueMaxPastDayTemperature : Double)

data class Wind (@SerializedName("Speed") val speed : Speed,
                 @SerializedName("Direction") val direction : Direction)
data class Speed (@SerializedName("Metric") val metricSpeed : MetricSpeed)
data class MetricSpeed (@SerializedName("Value") val valueSpeed : Double)
data class Direction (@SerializedName("Localized") val localDirectionText : String)


data class RealFeelTemperature (@SerializedName("Metric") val metricRealFeel : MetricRealFeel)
data class MetricRealFeel (@SerializedName("Value") val valueRealFeel : Double)
