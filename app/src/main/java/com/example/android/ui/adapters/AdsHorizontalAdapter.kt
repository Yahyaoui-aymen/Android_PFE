package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.responses.Ads

class AdsHorizontalAdapter (
    private val dataSet: List<Ads>,
    private val context: Context,
    ) : RecyclerView.Adapter<AdsHorizontalAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView : ImageView = view.findViewById(R.id.ads_image)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ads_list, viewGroup, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        val url = item.media
        Glide.with(context)
            .load(url)
            .into(viewHolder.imageView)



    }


    override fun getItemCount() = dataSet.size

}