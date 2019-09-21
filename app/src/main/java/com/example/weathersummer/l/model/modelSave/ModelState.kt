package com.example.weathersummer.l.model.modelSave

import com.example.weathersummer.l.model.City
import com.example.weathersummer.l.model.CurrentWeather
import com.example.weathersummer.l.model.DailyForecasts
import com.example.weathersummer.l.model.HoursWeather

data class ModelState(
    var weather: CurrentWeather,
    var city: City?,
    var hoursWeather: List<HoursWeather>,
    var dailyForecasts: List<DailyForecasts>
)