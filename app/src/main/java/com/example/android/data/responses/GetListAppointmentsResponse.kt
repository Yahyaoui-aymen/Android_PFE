package com.example.android.data.responses

data class GetListAppointmentsResponse(
    val `data`: List<Appointment>,
    val error: Any,
    val message: String
)