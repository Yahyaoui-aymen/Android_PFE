package com.example.android.ui.reservation

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.network.ReservationApi
import com.example.android.data.network.Resource
import com.example.android.data.repository.ReservationRepository
import com.example.android.data.requests.AppointmentRequest
import com.example.android.data.requests.NotificationRequest
import com.example.android.data.responses.Appointment
import com.example.android.data.responses.TimeSlot
import com.example.android.databinding.CalendarDayLayoutBinding
import com.example.android.databinding.CalendarHeaderBinding
import com.example.android.databinding.FragmentReservationBinding
import com.example.android.models.ReservationViewModel
import com.example.android.ui.*
import com.example.android.ui.adapters.TimeSlotGridAdapter
import com.example.android.ui.base.BaseFragment
import com.example.android.ui.client_home.ClientHomeActivity
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
import java.util.*

class ReservationFragment :
    BaseFragment<ReservationViewModel, FragmentReservationBinding, ReservationRepository>(){

    //calendar
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val selectionFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var confirmedAppointments: List<Appointment> = listOf()
    private var timeSlots: List<TimeSlot> = listOf()
    private var isOffer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.reservationFragment =this
        //extras user data
        var providerId = arguments?.getLong("provider_id")
        var firstName = arguments?.getString("first_name")
        var lastName = arguments?.getString("last_name")
        var government = arguments?.getString("government")
        var city = arguments?.getString("city")
        var phoneNumber = arguments?.getString("phoneNumber")
        var category = arguments?.getString("category")
        var imageUrl = arguments?.getString("imageUrl")
        isOffer = arguments?.getBoolean("isOffer") == true


        binding.username.text = lastName + "  " + firstName
        binding.category.text = category
        binding.location.text = government + " : " + city
        binding.phoneNumber.text = phoneNumber
        val url = DataSource.PHOTO_URL + imageUrl
        Glide.with(this)
            .load(url)
            .into(binding.img)
        //end extras



        //get confirmed appointments by provider id
        //confirmed appointment
        viewModel.getConfirmedAppointment(providerId!!)
        viewModel.confirmedAppointments.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    confirmedAppointments  = it.value.data
                    viewModel.getTimeSlots(providerId)
                }
            }
        })



        viewModel.timeSlots.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //TIMEsLOT adapter
                    val usedTimeSlots = confirmedAppointments
                        .filter { it.date.equals(selectionFormatter.format(today)) }
                        .map { item -> item.timeSlot }
                    val c = Calendar.getInstance()
                    val hour = c.get(Calendar.HOUR_OF_DAY)
                    timeSlots = it.value.data.sortedBy { it.id }

                    val  timeSlotsFiltered = timeSlots.filter { DataSource.timeConverter[it.time]!! > hour }
                    val timeSlotAdapter =
                        TimeSlotGridAdapter(timeSlotsFiltered, usedTimeSlots, requireContext(), "client")
                    binding.gridRecyclerView.adapter = timeSlotAdapter

                    timeSlotAdapter.setOnClickListener(object :
                        TimeSlotGridAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val timeSlot = timeSlotAdapter.getItemByPosition(position)
                            //show alert confirm reservation
                             val dialogBuilder =  Dialog(requireContext())

                            val view: View = layoutInflater.inflate(
                                com.example.android.R.layout.alert_reservation_ok,
                                null
                            )

                            dialogBuilder.setContentView(view)

                            view.findViewById<TextView>(R.id.provider_name).text = timeSlot.provider.firstName +
                                    " " +timeSlot.provider.lastName

                            view.findViewById<TextView>(R.id.hour).text = DataSource.getTime(timeSlot.time, requireContext())
                            view.findViewById<TextView>(R.id.date).text = getString(DataSource.dayConverter[today.dayOfWeek.toString()]!!) + " " + selectionFormatter.format(today)


                            view.findViewById<TextView>(R.id.confirmer).setOnClickListener {

                                reservation(timeSlot ,today )
                                view.findViewById<TextView>(R.id.reservation).visibility = View.VISIBLE
                                view.findViewById<TextView>(R.id.confirm_txt).visibility = View.GONE
                                view.findViewById<TextView>(R.id.message).visibility = View.VISIBLE
                                view.findViewById<TextView>(R.id.provider_name).visibility = View.VISIBLE
                                view.findViewById<TextView>(R.id.confirmer).visibility = View.GONE
                            }
                            view.findViewById<ImageView>(R.id.close).setOnClickListener{
                                dialogBuilder.dismiss()
                            }
                            dialogBuilder.getWindow()
                                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                            dialogBuilder.show()


                        }
                    })
                }
            }
        })


        //calendar
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek : DayOfWeek = DayOfWeek.SUNDAY
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

        binding.calendarView.daySize = CalendarView.sizeAutoWidth(dpToPx(28 , requireContext()))
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
                            textView.setTextColorRes(R.color.black)
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
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].name.first().toString()
                    }
                }
            }
        }


        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
        //end calendar

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
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)

            val usedTimeSlots = confirmedAppointments
                .filter { it.date.equals(selectionFormatter.format(date)) }
                .map { item -> item.timeSlot }

        if (date> today) {
            val timeSlotAdapter =
                TimeSlotGridAdapter(timeSlots, usedTimeSlots, requireContext(), "client")
            binding.gridRecyclerView.adapter = timeSlotAdapter
            timeSlotAdapter.setOnClickListener(object :
                TimeSlotGridAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {

                    val timeSlot = timeSlotAdapter.getItemByPosition(position)
                    //show alert confirm reservation
                    val dialogBuilder = Dialog(requireContext())

                    val view: View = layoutInflater.inflate(
                        com.example.android.R.layout.alert_reservation_ok,
                        null
                    )

                    dialogBuilder.setContentView(view)

                    view.findViewById<TextView>(R.id.provider_name).text =
                        timeSlot.provider.firstName +
                                " " + timeSlot.provider.lastName

                    view.findViewById<TextView>(R.id.hour).text =
                        DataSource.getTime(timeSlot.time, requireContext())
                    view.findViewById<TextView>(R.id.date).text =
                        getString(DataSource.dayConverter[date.dayOfWeek.toString()]!!) + " " + selectionFormatter.format(
                            date
                        )

                    //DataSource.dayConverter(date.dayOfWeek.toString())  + " "+ selectionFormatter.format(today)

                    view.findViewById<TextView>(R.id.confirmer).setOnClickListener {

                        reservation(timeSlot, date)
                        view.findViewById<TextView>(R.id.reservation).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.confirm_txt).visibility = View.GONE
                        view.findViewById<TextView>(R.id.message).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.provider_name).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.confirmer).visibility = View.GONE
                    }
                    view.findViewById<ImageView>(R.id.close).setOnClickListener {
                        dialogBuilder.dismiss()
                    }
                    dialogBuilder.getWindow()
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                    dialogBuilder.show()
                }
            })
        }else if (date<today){
            val timeSlotAdapter =
                TimeSlotGridAdapter(timeSlots, timeSlots, requireContext(), "client")
            binding.gridRecyclerView.adapter = timeSlotAdapter
        } else {

            val  timeSlotsFiltered = timeSlots.filter { DataSource.timeConverter[it.time]!! > hour }
            val timeSlotAdapter =
                TimeSlotGridAdapter(timeSlotsFiltered, usedTimeSlots, requireContext(), "client")
            binding.gridRecyclerView.adapter = timeSlotAdapter
            timeSlotAdapter.setOnClickListener(object :
                TimeSlotGridAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {

                    val timeSlot = timeSlotAdapter.getItemByPosition(position)
                    //show alert confirm reservation
                    val dialogBuilder = Dialog(requireContext())

                    val view: View = layoutInflater.inflate(
                        com.example.android.R.layout.alert_reservation_ok,
                        null
                    )

                    dialogBuilder.setContentView(view)

                    view.findViewById<TextView>(R.id.provider_name).text =
                        timeSlot.provider.firstName +
                                " " + timeSlot.provider.lastName

                    view.findViewById<TextView>(R.id.hour).text =
                        DataSource.getTime(timeSlot.time, requireContext())
                    view.findViewById<TextView>(R.id.date).text =
                        getString(DataSource.dayConverter[date.dayOfWeek.toString()]!!) + " " + selectionFormatter.format(
                            date
                        )

                    //DataSource.dayConverter(date.dayOfWeek.toString())  + " "+ selectionFormatter.format(today)

                    view.findViewById<TextView>(R.id.confirmer).setOnClickListener {

                        reservation(timeSlot, date)
                        view.findViewById<TextView>(R.id.reservation).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.confirm_txt).visibility = View.GONE
                        view.findViewById<TextView>(R.id.message).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.provider_name).visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.confirmer).visibility = View.GONE
                    }
                    view.findViewById<ImageView>(R.id.close).setOnClickListener {
                        dialogBuilder.dismiss()
                    }
                    dialogBuilder.getWindow()
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                    dialogBuilder.show()
                }
            })
        }

    }

    fun reservation(timeSlot: TimeSlot , date: LocalDate) {
        val title = "Prise des RDV"
        val message = "Vous avez une nouvelle demande de rendez-vous , veuillez répondre"
        val date = selectionFormatter.format(date)
        val reservationRequest = AppointmentRequest(date, timeSlot.id, isOffer)
        val saveNotificationRequest = NotificationRequest(title , message , timeSlot.provider.id)

        viewModel.addAppointment(if (timeSlot.provider.phoneToken.isNotEmpty()) timeSlot.provider.phoneToken.map { item -> item.token } else null,
            title, message, reservationRequest , saveNotificationRequest)
    }

    override fun getViewModel() = ReservationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReservationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ReservationRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(ReservationApi::class.java, token)
        return ReservationRepository(api)
    }

    fun goBack() {
        val intent = Intent(context, ClientHomeActivity::class.java)
        startActivity(intent)
    }

}