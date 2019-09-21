package com.example.weathersummer.l

import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.view.View
import com.example.weathersummer.WeatherView


interface BasePresenter {

    fun attachView(mvpView: Any?)

    fun viewIsReady(): Boolean

    fun detachView()

}