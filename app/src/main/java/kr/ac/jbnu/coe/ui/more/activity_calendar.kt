package kr.ac.jbnu.coe.ui.more

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applikeysolutions.cosmocalendar.view.CalendarView
import kr.ac.jbnu.coe.R
import java.util.*

class activity_calendar : AppCompatActivity(){
    lateinit var calendarView : CalendarView
    lateinit var startDate : String
    lateinit var endDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_calendar)

        calendarView = findViewById(R.id.calendarLL)

    }
}