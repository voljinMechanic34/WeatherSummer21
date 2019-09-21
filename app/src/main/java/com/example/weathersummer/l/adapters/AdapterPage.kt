package com.example.weathersummer.l.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.weathersummer.l.model.modelSave.ModelState
import com.example.weathersummer.l.Utils
import com.example.weathersummer.l.model.CurrentWeather
import com.example.weathersummer.l.model.DailyForecasts
import com.example.weathersummer.l.model.HoursWeather
import com.white.progressview.CircleProgressView
import kotlinx.android.synthetic.main.item_page.view.*

class AdapterPage : PagerAdapter() {
    lateinit var  adapterItem : AdapterItem
    lateinit var  adapterItemDaily : AdapterDaily
    var views = ArrayList<View>()
    val k : Array<String> = arrayOf("1","2","3","4")
    var list : ArrayList<String> = ArrayList()
    var listSp: ArrayList<ModelState> = ArrayList()

    override fun getItemPosition(`object`: Any): Int {
        val index = views.indexOf(`object`)
        return if (index == -1)
            POSITION_NONE
        else
            index
    }
    fun removeViews (pager: ViewPager) : Int{
        pager.adapter = null
        views.clear()
        pager.adapter = this

        return views.size
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = views[position]
       // v.textView.text = list[position]
        initRecycler(v)
        getWeather(listSp[position].weather,v)
        getHoursWeather(listSp[position].hoursWeather)
        getDailyWeather(listSp[position].dailyForecasts)
        //Log.i("TAG/I",list[position])
        v.setTag(position)
        container.addView(v)

        return v
    }

    fun setList(item: List<ModelState>){
        listSp = item as ArrayList<ModelState>
    }
    fun setItem(item: ModelState, position: Int){
        listSp[position] = item
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views.get(position))
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.size
    }
    fun addView(v : View) : Int{
        return addView(v,views.size)
    }
    fun addView(v: View, position: Int): Int {
        views.add(position, v)
        return position
    }
    fun addView2(v: View, position: Int): Int {
        views[position] = v
        return position
    }
    fun removeView(pager: ViewPager, v: View): Int {
        return removeView(pager, views.indexOf(v))
    }
    fun removeView(pager: ViewPager, position: Int): Int {
        pager.adapter = null
        views.removeAt(position)
        pager.adapter = this

        return position
    }
    fun getView(position: Int): View {
        return views[position]
    }
    fun initRecycler(v: View) {
        v.recyclerView_Hours.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL,false)
        adapterItem = AdapterItem(v.context)
        v.recyclerView_Hours.adapter = adapterItem
        v.recyclerView_Daily.layoutManager = LinearLayoutManager(v.context)
        adapterItemDaily = AdapterDaily(v.context)
        v.recyclerView_Daily.adapter = adapterItemDaily
    }
    fun getWeather(weather: CurrentWeather?,view: View){
        val tempCityP = weather?.temperatature?.metric?.value.toString()+"°C"

        view.tempCity.text = tempCityP
        view.weatherText.text = weather?.wetherText
        val minAndMaxTemp = weather?.tempSummary?.pastDayTemperature?.maxPastDayTemperature?.metricMaxPastDayTemperature?.valueMaxPastDayTemperature.toString()+"°C/"+
                weather?.tempSummary?.pastDayTemperature?.minPastDayTemperature?.metricMinPastDayTemperature?.valueMinPastDayTemperature.toString()+"°C"
        view.tempCityMax.text = minAndMaxTemp
        view.imageWeather.setImageResource(Utils.getImageWeather(weather?.WeatherIcon))
        //view.relativeLayout.setBackgroundResource(Utils.getBackgroundForActivity(weather?.isDayTime))
        //view.swipeRefreshLayout.setBackgroundResource(Utils.getBackgroundForActivity(weather?.isDayTime))
        val progressBar = view.circleProgressBar as CircleProgressView
        progressBar.progress = weather?.humidity!!
        progressBar.display
        val feelTempValue = "${weather.realFeelTemp.metricRealFeel.valueRealFeel}°"
        view.textFeelTempValue.text = feelTempValue
        val ufValue = "${weather.uvIndex} ${weather.uvIndexText}"
        view.textUfValue.text = ufValue

        view.textDirectionValue.text = weather.wind.direction.localDirectionText
        val speedValue = "${weather.wind.speed.metricSpeed.valueSpeed} км/ч"
        view.textSpeedValue.text = speedValue
        //view.listWeather.add(weather)
    }
    fun getHoursWeather(hoursWeather: List<HoursWeather>) {

            adapterItem.setListItem(hoursWeather)
            adapterItem.notifyDataSetChanged()

    }
    fun getDailyWeather(dailyForecasts: List<DailyForecasts>) {
         adapterItemDaily.setListItem(dailyForecasts)
         adapterItemDaily.notifyDataSetChanged()
    }

}