package com.example.weathersummer.l.model

import com.google.gson.annotations.SerializedName

data class City(
    val LocalizedName : String?,
    val TimeZone : TimeZone,
    @SerializedName ("Key") val key : String?
)