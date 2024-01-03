package com.Bastory.kurdishdatecalculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.Bastory.kurdishdatecalculator.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentDate = LocalDate.now()

        val currentDay = currentDate.dayOfMonth
        val currentMonth = currentDate.monthValue
        val currentYear = currentDate.year

        val months : List<String> = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val EnglishDay = findViewById<TextView>(R.id.gregorianDay)
        EnglishDay.setText(currentDay.toString())

        val EnglishMonth = findViewById<TextView>(R.id.gregorianMonth)
        EnglishMonth.setText(months[currentMonth - 1])

        val EnglishYear = findViewById<TextView>(R.id.gregorianYear)
        EnglishYear.setText(currentYear.toString())


        val KurdishDate : List<String> = getDate()

        val month = findViewById<TextView>(R.id.month)
        month.setText(KurdishDate[0])

        val day = findViewById<TextView>(R.id.day)
        day.setText(KurdishDate[1])

        val year = findViewById<TextView>(R.id.year)
        year.setText(KurdishDate[2])

        var hijriDate = gregorianToHijri(currentYear, currentMonth, currentDay);
        val hijri = findViewById<TextView>(R.id.hijri)
        hijri.setText(hijriDate)

        val timer = findViewById<TextView>(R.id.timer)
        var time = getCurrentTime().toString().replace("\\.\\d+".toRegex(), "")
        timer.setText(time)

        handler.post(object : Runnable {
            override fun run() {
                timer.setText(getCurrentTime().toString().replace("\\.\\d+".toRegex(), ""))
                handler.postDelayed(this, 1000) // 1000 milliseconds (1 second)
            }
        })
    }
    private fun getCurrentTime(): LocalTime {
        return LocalTime.now()
    }
    private fun gregorianToHijri(year: Int, month: Int, day: Int): String {
        val gregorianDate = LocalDate.of(year, month, day)
        val hijrahDate = HijrahDate.from(gregorianDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.US)
        val formattedHijriDate = formatter.format(hijrahDate)

        return formattedHijriDate
    }
    private fun getDate(): List<String>{

        val currentDate = LocalDate.now()

        val currentMonth = currentDate.monthValue

        val kurdishMonth : List<String> = listOf("نەورۆز", "گوڵان", "جۆزەردان", "پوشپەڕ", "گەلاوێژ", "خەرمانان", "ڕەزبەر", "گەڵاڕێزان", "سەرماوەرز", "بەفرانبار", "ڕیبەندان", "ڕەشەمێ");
        val year = Year.of(YearMonth.now().year)
        val totalDays = year.length()

        val firstSixMonths = 31
        val seventhToElevenths = 30
        var twelveth : Int = 29
        if(totalDays == 366){
              twelveth = 30
        }
        val today = LocalDate.now()
        val kurdishDate = LocalDate.of(today.year, 3, 21)
        var daysDifference = ChronoUnit.DAYS.between(kurdishDate, today)
        if(daysDifference < 0){
            daysDifference = -daysDifference
        }
        var month = 0;
        var found = false;
        while(!found){
            if(month <= 6){
                if(daysDifference - firstSixMonths > 0){
                    daysDifference -= firstSixMonths
                    println("Executing first six month at $month and $daysDifference days left")
                    month++
                }
            }
            if(month <= 11 && month > 6){
                if(daysDifference - seventhToElevenths > 0){
                    daysDifference -= seventhToElevenths
                    month++
                }
            }

            if(month > 11){
                daysDifference -= twelveth
                month++
            }

            if(daysDifference < 29){
                found = true;
            }
        }

        var kurdishYear : Int = today.year + 700;
        if(month > 9 && month < 3){
            kurdishYear = today.year + 699
        }
        daysDifference += 2
        return listOf(kurdishMonth[month], daysDifference.toString(), kurdishYear.toString());
    }

}