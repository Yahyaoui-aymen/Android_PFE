package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.DataSource
import com.example.android.data.responses.Offer

class OfferAdapter(
    private val dataSet: List<Offer>,
    private val context: Context,
    private val numberofItem : Int?
) : RecyclerView.Adapter<OfferAdapter.ViewHolder>() {
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
        if (dataSet.isNotEmpty()) {
            val item = dataSet[position]
            val url = DataSource.PHOTO_URL + item.image
            Glide.with(context)
                .load(url)
                .into(viewHolder.serviceImg)
            viewHolder.serviceTitle.text = item.title
            viewHolder.serviceDesc.text = item.description
        }

    }

    override fun getItemCount(): Int {
        if (numberofItem != null)
        { if (numberofItem<dataSet.size) return numberofItem else return dataSet.size}
        else return dataSet.size

    }
/*if (dataSet.isNotEmpty())

        if (numberofItem< dataSet.size)numberofItem ?: dataSet.size


                                    else 0*/


}