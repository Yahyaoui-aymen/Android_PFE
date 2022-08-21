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

class CategoryButtonAdapter(
    private val dataSet: List<Category>,
    private val context: Context,
) : RecyclerView.Adapter<CategoryButtonAdapter.ViewHolder>() {
    class ViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val buttonTxt: TextView = view.findViewById(R.id.buttonTxt)
        val buttonIcon: ImageView = view.findViewById(R.id.buttonIcon)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick (position :Int)
    }

    fun setOnClickListener(listener: CategoryButtonAdapter.onItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category_button_list, viewGroup, false)
        return CategoryButtonAdapter.ViewHolder(adapterLayout , mListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.buttonTxt.text = item.name
        viewHolder.buttonIcon.setImageResource(item.icon)

    }


    override fun getItemCount() = dataSet.size


    fun getItemIdByPosition (position: Int) : Int {
        return dataSet[position].id
    }

}