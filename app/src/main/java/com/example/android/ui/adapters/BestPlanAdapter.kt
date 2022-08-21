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
import com.example.android.data.CategoryData
import com.example.android.data.DataSource
import com.example.android.data.responses.Offer

class BestPlanAdapter(
    private val dataSet: List<Offer>,
    private val context: Context,
): RecyclerView.Adapter<BestPlanAdapter.ViewHolder>()  {

    private lateinit var mListener: onItemClickListener


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_best_plan, viewGroup, false)
        return ViewHolder(adapterLayout, mListener)
    }

    override fun onBindViewHolder(viewHolder:ViewHolder, position: Int) {
        val item = dataSet[position]
        val url = DataSource.PHOTO_URL+item.user.imageUrl
        Glide.with(context)
            .load(url)
            .into(viewHolder.offerImg)
        viewHolder.provider_txt.text = item.user.firstName +" "+ item.user.lastName
        viewHolder.provider_cat_txt.text = CategoryData(context).categories.filter { it.id == item.user.category.id }.first().name
        viewHolder.offer_txt.text = item.title
        viewHolder.offer_subtitle_txt.text = item.subtitle
        viewHolder.price_txt.text= item.price
    }

    override fun getItemCount() = if (dataSet.size<100) dataSet.size else 100




    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val offerImg: ImageView = view.findViewById(R.id.offer_img)
        val provider_txt: TextView = view.findViewById(R.id.provider_txt)
        val provider_cat_txt: TextView = view.findViewById(R.id.provider_cat_txt)
        val offer_txt: TextView = view.findViewById(R.id.offer_txt)
        val offer_subtitle_txt: TextView = view.findViewById(R.id.offer_subtitle_txt)
        val price_txt: TextView = view.findViewById(R.id.price_txt)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition )
            }
        }

    }


    interface onItemClickListener  {
        fun onItemClick (position :Int)
    }

    fun setOnClickListener(listener : onItemClickListener){
        mListener = listener
    }


    fun getItemIdByPosition (position: Int) : Int {
        return dataSet[position].id
    }
    fun getItemByPosition (position: Int) : Offer {
        return dataSet[position]
    }

}