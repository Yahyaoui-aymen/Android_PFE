package com.example.android.ui.provider_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.example.android.R
import com.example.android.data.network.ProviderApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ProviderRepository
import com.example.android.data.responses.Appointment
import com.example.android.data.responses.TimeSlot
import com.example.android.databinding.CalendarDayLayoutBinding
import com.example.android.databinding.CalendarHeaderBinding
import com.example.android.databinding.FragmentProviderTimeslotBinding
import com.example.android.models.ProviderViewModel
import com.example.android.ui.*
import com.example.android.ui.adapters.TimeSlotGridAdapter
import com.example.android.ui.base.BaseFragment
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class ProviderTimeslotFragment :
    BaseFragment<ProviderViewModel, FragmentProviderTimeslotBinding, ProviderRepository>() {
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var confirmedAppointments: List<Appointment> = listOf()
    private var timeSlots: List<TimeSlot> = listOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.providerTimeslotFragment = this


        //get this provider confirmed appointments
        //confirmed appointment
        viewModel.getConfirmedAppointment()
        viewModel.confirmedAppointment.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    confirmedAppointments = it.value.data
                    viewModel.getProviderTimeSlot()
                }
            }
        })

        //get all timeSlots for this provider
        viewModel.timeSlots.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //TIMEsLOT adapter
                    val usedTimeSlots = confirmedAppointments
                        .filter { it.date.equals(selectionFormatter.format(today)) }
                        .map { item -> item.timeSlot }.sortedBy { it.id }
                    timeSlots = it.value.data
                    val timeSlotAdapter =
                        TimeSlotGridAdapter(timeSlots, usedTimeSlots, requireContext() , "provider")
                    binding.gridRecyclerView.adapter = timeSlotAdapter

                    timeSlotAdapter.setOnClickListener(object :
                        TimeSlotGridAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                        }
                    })
                }
            }
        })





        //CALENDAAAAAAR
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY
        val daysOfWeek = daysOfWeekFromLocale()

        binding.calendarView.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.calendarView.post {
                // Show today's events initially.
                selectDate(today)
            }
        }


        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayLayoutBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectDate(day.date)
                    }
                }
            }
        }


        binding.calendarView.daySize = CalendarView.sizeAutoWidth(dpToPx(30, requireContext()))
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.dayText
                val dotView = container.binding.dotView
                textView.text = day.date.dayOfMonth.toString()
                textView.text = day.date.dayOfMonth.toString()



                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.today_bg)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(androidx.appcompat.R.color.material_blue_grey_800)
                            textView.setBackgroundResource(R.drawable.selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.textBlueColor)
                            textView.background = null
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }

                if (day.date.dayOfWeek == firstDayOfWeek) {
                    textView.setTextColorRes(R.color.red)
                    textView.alpha = 0.3f
                }
            }
        }

        binding.calendarView.monthScrollListener = {
            val title = "${monthTitleFormatter.format(it.yearMonth)} ${it.yearMonth.year}"
            binding.monthYearText.text = title
            // Select the first day of the month when
            // we scroll to a new month.
            selectDate(it.yearMonth.atDay(1))
        }

        binding.nextMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.previousMonthImage.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.calendarView.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].name.first().toString()
                        }
                }
            }
        }


        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        val usedTimeSlots = confirmedAppointments
            .filter { it.date.equals(selectionFormatter.format(date)) }
            .map { item -> item.timeSlot }.sortedBy { it.id }
        val timeSlotAdapter =
            TimeSlotGridAdapter(timeSlots, usedTimeSlots, requireContext() , "provider")
        binding.gridRecyclerView.adapter = timeSlotAdapter
        timeSlotAdapter.setOnClickListener(object :
            TimeSlotGridAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

    }


    override fun getViewModel() = ProviderViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProviderTimeslotBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProviderRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ProviderApi::class.java, token)
        return ProviderRepository(api)
    }

}