package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.data.responses.Notification
import java.text.SimpleDateFormat
import java.util.*


class NotificationAdapter(
    private val dataSet: List<Notification>,
    private val context: Context,
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>()  {



    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_notification_list, viewGroup, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(viewHolder:ViewHolder, position: Int) {
        viewHolder.notifDate.text = getDateOnly(dataSet[position].date)
        viewHolder.notifTime.text = getTimeOnly(dataSet[position].date)
        viewHolder.notifTitle.text = dataSet[position].title
        viewHolder.notifMessage.text = dataSet[position].message

        if (position >= 1 && getDateOnly(dataSet[position].date).equals(getDateOnly(dataSet[position-1].date))) {
            viewHolder.notifDate.visibility = View.GONE

        }
    }

    override fun getItemCount() = if (dataSet.size<10) dataSet.size else 10





    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val notifDate = view.findViewById<TextView>(R.id.notif_date_txt)
        val notifTime = view.findViewById<TextView>(R.id.notif_time_txt)
        val notifTitle = view.findViewById<TextView>(R.id.notif_title_txt)
        val notifMessage = view.findViewById<TextView>(R.id.notif_message_txt)

    }



    fun getItemIdByPosition (position: Int) : Int {
        return dataSet[position].id
    }
    fun getItemByPosition (position: Int) : Notification {
        return dataSet[position]
    }
    fun getDateOnly(date: Date):String{
        val dateOnly = SimpleDateFormat("yyyy-MM-dd")
        return dateOnly.format(date)
    }
    fun getTimeOnly(date: Date) : String{
        val timeOnly = SimpleDateFormat("HH:mm")
        return timeOnly.format(date)
    }

}