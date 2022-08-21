package com.example.android.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.responses.Appointment
import com.example.android.data.responses.PhoneToken
import com.example.android.models.ProviderViewModel

class AppointmentAdapter(
    private val dataSet: List<Appointment>,
    private val context: Context,
    private val status: Int,
    private val viewModel: ProviderViewModel,
    //to click on child Item
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    private lateinit var item: Appointment
    private lateinit var itemList: List<Appointment>




    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_appointment_list, viewGroup, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when(status) {
            1-> {
                itemList = dataSet.filter { it.status.id == 2  }
                item = itemList[position]
                getItemIcons(item,viewHolder)
            }
            2-> {
                itemList=dataSet.filter { it.status.id == 1 }
                item = itemList[position]
                getItemIcons(item,viewHolder)
            }
            3-> {
                itemList = dataSet.filter { it.status.id == 3 }
                item = itemList[position]
                getItemIcons(item,viewHolder)
            }
            else -> {
                itemList = dataSet.sortedBy { it.status.id }
                item = itemList[position]
                getItemIcons(item,viewHolder)
            }
        }

        viewHolder.offer_detect.visibility = if (item.isOffer) View.VISIBLE else View.GONE





        viewHolder.clientName.text = item.client.firstName +
                " " + item.client.lastName
        viewHolder.appointmentDate.text = item.date
        viewHolder.appointmentTime.text = DataSource.time[item.timeSlot.time]


        //click on childItem
        viewHolder.check_icon.setOnClickListener {
            val clientPhoneTokens = itemList[position].client.phoneToken
            val clientId = itemList[position].client.id
            itemClickListener.confirm(itemList[position] ,
                if (clientPhoneTokens.isNotEmpty()) clientPhoneTokens.map { item -> item.token } else null , clientId)
        }

        //click on childItem
        viewHolder.reject_icon.setOnClickListener {
            val clientPhoneTokens = itemList[position].client.phoneToken
            val clientId = itemList[position].client.id
            itemClickListener.reject(itemList[position],
                if (clientPhoneTokens.isNotEmpty()) clientPhoneTokens.map { item -> item.token } else null , clientId)
        }

        //click on childItem
        viewHolder.remove_icon.setOnClickListener {
            itemClickListener.remove(itemList[position].id)
        }

    }


    override fun getItemCount(): Int {
        when(status) {
            1 -> {
                return dataSet.filter { it.status.id == 2 }.size
            }
            2 -> {
                return dataSet.filter { it.status.id == 1 }.size
            }
            3 -> {
                return dataSet.filter { it.status.id == 3 }.size
            }
            else -> {
                return dataSet.size
            }
        }
    }

    fun getItemIcons(item : Appointment, viewHolder: ViewHolder) {
        when(item.status.id) {
            2-> {
                viewHolder.confirmed_icon.visibility = View.GONE
                viewHolder.check_icon.visibility = View.VISIBLE
                viewHolder.reject_icon.visibility = View.VISIBLE
                viewHolder.remove_icon.visibility = View.GONE
            }
            1-> {
                viewHolder.confirmed_icon.visibility = View.VISIBLE
                viewHolder.check_icon.visibility = View.GONE
                viewHolder.reject_icon.visibility = View.GONE
                viewHolder.remove_icon.visibility = View.GONE
            }
            3-> {
                viewHolder.confirmed_icon.visibility = View.GONE
                viewHolder.check_icon.visibility = View.GONE
                viewHolder.reject_icon.visibility = View.GONE
                viewHolder.remove_icon.visibility = View.VISIBLE
            }
        }

    }





    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clientName = view.findViewById<TextView>(R.id.client_name_txt)
        val appointmentDate = view.findViewById<TextView>(R.id.date_txt)
        val appointmentTime = view.findViewById<TextView>(R.id.time_txt)
        val reject_icon = view.findViewById<ImageView>(R.id.reject_icon)
        val check_icon = view.findViewById<ImageView>(R.id.check_icon)
        val remove_icon = view.findViewById<ImageView>(R.id.rejected_icon)
        val confirmed_icon = view.findViewById<ImageView>(R.id.confirmed_icon)
        val offer_detect = view.findViewById<ImageView>(R.id.offer_detect)

    }

    //Click on child Item
    interface ItemClickListener {
        fun confirm(item: Appointment, clientPhoneToken: List<String>? , userId : Long)

        fun reject(item: Appointment, clientPhoneToken: List<String>? , userId : Long)

        fun remove(itemId: Long)
    }




}