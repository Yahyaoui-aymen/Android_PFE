package com.example.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.R
import com.example.android.data.CategoryData
import com.example.android.data.DataSource
import com.example.android.data.responses.Ads
import com.example.android.data.responses.Category
import com.example.android.data.responses.User

class SearchCategoryAdapter(
    private val dataSet: List<User>,
    private val requireContext: Context,
) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View , listener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        val img: ImageView = view.findViewById(R.id.image)
        val name: TextView = view.findViewById(R.id.name)
        val category: TextView = view.findViewById(R.id.category)
        val phoneNumber: TextView = view.findViewById(R.id.phoneNumber)
        val location: TextView = view.findViewById(R.id.location)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition )
            }
        }
    }

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick (position :Int)
    }

    fun setOnClickListener(listener : onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_saerch, parent, false)
        return SearchCategoryAdapter.ViewHolder(adapterLayout , mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        if (item.imageUrl != null) {
            val url = DataSource.PHOTO_URL + item.imageUrl
            Glide.with(requireContext)
                .load(url)
                .into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.ic_user_solid)
        }
        holder.name.text = item.firstName + " " + item.lastName
        holder.category.text = CategoryData(requireContext).categories.filter { it.id == item.category.id }.first().name
        holder.phoneNumber.text = item.phoneNumber
        holder.location.text = item.government + " : " + item.city
    }

    override fun getItemCount() = dataSet.size
}
