package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.data.responses.Category

class ServiceAdapter(
    private val dataSet: List<Category>,
    private val context: Context,
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val serviceImg: ImageView = view.findViewById(R.id.service_img)
        val serviceTitle : TextView = view.findViewById(R.id.service_title)
        val serviceDesc: TextView = view.findViewById(R.id.service_desc)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_service_list, viewGroup, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.serviceImg.setImageResource(R.drawable.image2)
        viewHolder.serviceTitle.text = item.name
        viewHolder.serviceDesc.text = item.name

    }


    override fun getItemCount() = if (dataSet.size<3) dataSet.size else 3


}