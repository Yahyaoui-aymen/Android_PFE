package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.responses.TimeSlot
import com.google.android.material.card.MaterialCardView

class TimeSlotGridAdapter(
    private val dataSet: List<TimeSlot>,
    private val usedTimeSlots: List<TimeSlot>,
    private val context: Context,
    private val role: String
) : RecyclerView.Adapter<TimeSlotGridAdapter.ViewHolder>() {

    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        val txt: TextView = view.findViewById(R.id.time_txt)
        val card: MaterialCardView = view.findViewById(R.id.card)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): TimeSlotGridAdapter.ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_time_slot, viewGroup, false)
        return ViewHolder(adapterLayout, mListener)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = if(role.equals("provider")) dataSet else dataSet.minus(usedTimeSlots)
        val item =data[position]

        if (usedTimeSlots.contains(item)) {
                    viewHolder.card.setCardBackgroundColor(context.resources.getColor(R.color.white))
                    viewHolder.txt.setTextColor(context.resources.getColor(R.color.textBlueColor))

        }


        viewHolder.txt.text = DataSource.getTime(item.time, context)

    }

    override fun getItemCount() = if (role.equals("provider")) dataSet.size else dataSet.minus(usedTimeSlots).size

    fun getItemIdByPosition(position: Int): Int {
        return dataSet[position].id
    }



    fun getItemByPosition (position: Int) : TimeSlot {
        return  if (role.equals("provider")) dataSet[position] else dataSet.minus(usedTimeSlots)[position]
    }
}