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

class CategoryHorizontalAdapter(
    private val dataSet: List<Category>,
    private val context: Context
) : RecyclerView.Adapter<CategoryHorizontalAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTxt: TextView = view.findViewById(R.id.category_txt)
        val categoryImg: ImageView = view.findViewById(R.id.category_image)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category_list, viewGroup, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.categoryTxt.text = item.name
        viewHolder.categoryImg.setImageResource(R.drawable.ic_baseline_laptop_24)

    }


    override fun getItemCount() = dataSet.size


}