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

class CategoryGridAdapter(
    private val dataSet: List<Category>,
    private val context : Context
    ) : RecyclerView.Adapter<CategoryGridAdapter.ViewHolder>() {

    class ViewHolder(view: View , listener: onItemClickListener) : RecyclerView.ViewHolder(view) {

        val image : ImageView = view.findViewById(R.id.image_category)
        val txt : TextView = view.findViewById(R.id.text_category)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition )
            }
        }
    }

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener  {
         fun onItemClick (position :Int)
    }

    fun setOnClickListener(listener : onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CategoryGridAdapter.ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category_grid, viewGroup, false)
        return ViewHolder(adapterLayout , mListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.txt.text = item.name
        viewHolder.image.setImageResource(item.icon)
    }

    override fun getItemCount() = dataSet.size

    fun getItemIdByPosition (position: Int) : Int {
        return dataSet[position].id
    }
}