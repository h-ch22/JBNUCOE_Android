package kr.ac.jbnu.coe.ui.Calendar
//
//import android.os.Bundle
//import android.util.TypedValue
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.annotation.ColorRes
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.kizitonwose.calendarview.model.CalendarDay
//import com.kizitonwose.calendarview.model.CalendarMonth
//import com.kizitonwose.calendarview.model.DayOwner
//import com.kizitonwose.calendarview.ui.DayBinder
//import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
//import com.kizitonwose.calendarview.ui.ViewContainer
//import kr.ac.jbnu.coe.R
//import java.time.LocalDate
//import java.time.LocalDateTime
//import java.time.YearMonth
//import java.time.format.DateTimeFormatter
//import java.time.format.TextStyle
//import java.util.*
//
//data class Flight(val time: LocalDateTime, val departure: Airport, val destination: Airport, @ColorRes val color: Int) {
//    data class Airport(val city: String, val code: String)
//}
//
//class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
//
//    val flights = mutableListOf<Flight>()
//
//    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
//        return CalendarViewHolder(
//                CalendarViewBinding.inflate(parent.context.layoutInflater, parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(viewHolder: CalendarViewHolder, position: Int) {
//        viewHolder.bind(flights[position])
//    }
//
//    override fun getItemCount(): Int = flights.size
//
//    inner class CalendarViewHolder(val binding: CalendarViewBinding) :
//            RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(flight: Flight) {
//            binding.itemFlightDateText.apply {
//                text = formatter.format(flight.time)
//                setBackgroundColor(itemView.context.getColorCompat(flight.color))
//            }
//
//            binding.itemDepartureAirportCodeText.text = flight.departure.code
//            binding.itemDepartureAirportCityText.text = flight.departure.city
//
//            binding.itemDestinationAirportCodeText.text = flight.destination.code
//            binding.itemDestinationAirportCityText.text = flight.destination.city
//        }
//    }
//}
//
//class CalendarFragment : Fragment(){
//
//    private var selectedDate: LocalDate? = null
//    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
//
//    private val flightsAdapter = CalendarAdapter()
//    private val flights = generateFlights().groupBy { it.time.toLocalDate() }
//
//    private lateinit var binding: Example5FragmentBinding
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = Example5FragmentBinding.bind(view)
//
//        binding.exFiveRv.apply {
//            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//            adapter = flightsAdapter
//            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
//        }
//
//        flightsAdapter.notifyDataSetChanged()
//
//        val daysOfWeek = daysOfWeekFromLocale()
//
//        val currentMonth = YearMonth.now()
//        binding.exFiveCalendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
//        binding.exFiveCalendar.scrollToMonth(currentMonth)
//
//        class DayViewContainer(view: View) : ViewContainer(view) {
//            lateinit var day: CalendarDay // Will be set when this container is bound.
//            val binding = Example5CalendarDayBinding.bind(view)
//            init {
//                view.setOnClickListener {
//                    if (day.owner == DayOwner.THIS_MONTH) {
//                        if (selectedDate != day.date) {
//                            val oldDate = selectedDate
//                            selectedDate = day.date
//                            val binding = this@CalendarFragment.binding
//                            binding.exFiveCalendar.notifyDateChanged(day.date)
//                            oldDate?.let { binding.exFiveCalendar.notifyDateChanged(it) }
//                            updateAdapterForDate(day.date)
//                        }
//                    }
//                }
//            }
//        }
//
//        binding.exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//            override fun bind(container: DayViewContainer, day: CalendarDay) {
//                container.day = day
//                val textView = container.binding.exFiveDayText
//                val layout = container.binding.exFiveDayLayout
//                textView.text = day.date.dayOfMonth.toString()
//
//                val flightTopView = container.binding.exFiveDayFlightTop
//                val flightBottomView = container.binding.exFiveDayFlightBottom
//                flightTopView.background = null
//                flightBottomView.background = null
//
//                if (day.owner == DayOwner.THIS_MONTH) {
//                    textView.setTextColorRes(R.color.calendar_text_grey)
//                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.calendar_selected_bg else 0)
//
//                    val flights = flights[day.date]
//                    if (flights != null) {
//                        if (flights.count() == 1) {
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                        } else {
//                            flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[1].color))
//                        }
//                    }
//                } else {
//                    textView.setTextColorRes(R.color.calendar_text_grey_light)
//                    layout.background = null
//                }
//            }
//        }
//
//        class MonthViewContainer(view: View) : ViewContainer(view) {
//            val legendLayout = Example5CalendarHeaderBinding.bind(view).legendLayout.root
//        }
//        binding.exFiveCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
//            override fun create(view: View) = MonthViewContainer(view)
//            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
//                // Setup each header day text if we have not done that already.
//                if (container.legendLayout.tag == null) {
//                    container.legendLayout.tag = month.yearMonth
//                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                        tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
//                                .toUpperCase(Locale.ENGLISH)
//                        tv.setTextColorRes(R.color.calendar_text_grey)
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
//                    }
//                    month.yearMonth
//                }
//            }
//        }
//
//        binding.exFiveCalendar.monthScrollListener = { month ->
//            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
//            binding.exFiveMonthYearText.text = title
//
//            selectedDate?.let {
//                // Clear selection if we scroll to a new month.
//                selectedDate = null
//                binding.exFiveCalendar.notifyDateChanged(it)
//                updateAdapterForDate(null)
//            }
//        }
//
//        binding.exFiveNextMonthImage.setOnClickListener {
//            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
//                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
//            }
//        }
//
//        binding.exFivePreviousMonthImage.setOnClickListener {
//            binding.exFiveCalendar.findFirstVisibleMonth()?.let {
//                binding.exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
//            }
//        }
//    }
//
//    private fun updateAdapterForDate(date: LocalDate?) {
//        flightsAdapter.flights.clear()
//        flightsAdapter.flights.addAll(flights[date].orEmpty())
//        flightsAdapter.notifyDataSetChanged()
//    }
//}