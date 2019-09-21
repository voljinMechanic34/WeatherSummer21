package com.example.weathersummer.l

import com.example.weathersummer.R
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {
        fun getImageWeather(value: Int?): Int {

            return when (value) {
                1 -> R.drawable.one
                2 -> R.drawable.two
                3 -> R.drawable.three
                4 -> R.drawable.four
                5 -> R.drawable.five
                6 -> R.drawable.six
                7 -> R.drawable.seven
                8 -> R.drawable.eight
                11 -> R.drawable.eleven
                12 -> R.drawable.twelve
                13 -> R.drawable.thirtheen
                14 -> R.drawable.fourteen
                15 -> R.drawable.fifteen
                16 -> R.drawable.sixteen
                17 -> R.drawable.seventeen
                18 -> R.drawable.eighteen
                19 -> R.drawable.nineteen
                20 -> R.drawable.twenty
                21 -> R.drawable.twentyone
                22 -> R.drawable.twentytwo
                23 -> R.drawable.twentythree
                24 -> R.drawable.twentyfour
                25 -> R.drawable.twentyfive
                26 -> R.drawable.twentysix
                29 -> R.drawable.twentynine
                30 -> R.drawable.thirty
                31 -> R.drawable.thirtyone
                32 -> R.drawable.thirtytwo
                33 -> R.drawable.thirtythree
                34 -> R.drawable.thirtyfour
                35 -> R.drawable.thirtyfive
                36 -> R.drawable.thirtysix
                37 -> R.drawable.thirtyseven
                38 -> R.drawable.thirtyeight
                39 -> R.drawable.thirtynine
                40 -> R.drawable.fourty
                41 -> R.drawable.fourtyone
                42 -> R.drawable.fourtytwo
                43 -> R.drawable.fourtythree
                44 -> R.drawable.fourtyfour

                else -> R.drawable.one
            }
        }
        fun getBackgroundForActivity(value: Boolean?) : Int{
            return if (value!!)
                R.drawable.day
            else R.drawable.night
        }
        fun getRealTime(value : Long):String{
            val date = Date(value * 1000L)
            val formatter = SimpleDateFormat("HH:mm")
            formatter.timeZone = TimeZone.getTimeZone("GMT+4")
            val timeH = formatter.format(date)
            return timeH
        }
        fun getRealWeather(value : Double):String{
           return  String.format("%.1f°", (value - 32) / 1.8)
        }

        fun getRealTimeDaily(timeDaily: Long): CharSequence? {
            val date2 = Date(timeDaily * 1000L)
            val formatter2 = SimpleDateFormat("EEE, d MMM")
            formatter2.timeZone = TimeZone.getTimeZone("GMT+4")
            val myDate = formatter2.format(date2)
            return myDate
        }
    }
}