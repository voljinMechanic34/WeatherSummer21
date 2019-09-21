package com.example.weathersummer

import com.example.weathersummer.l.model.modelSave.ModelState

interface WeatherView {
        fun showLoading()
        fun hideLoading()
        fun setData(model : ModelState)
        fun onErrorLoading(message:String)
        fun requestPermission()
        fun permissionGiven() : Boolean
        fun updateLocation()
}