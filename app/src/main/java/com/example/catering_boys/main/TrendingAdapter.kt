package com.example.catering_boys.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catering_boys.databinding.ListItemTrendingBinding // Import binding class

class TrendingAdapter(
    private val ctx: Context,
    private var modelTrendingList: List<ModelTrending>
) : RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {


    fun setFilter(filterList: List<ModelTrending>) {
        this.modelTrendingList = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ListItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelTrendingList[position]


        holder.binding.apply {
            imgThumb.setImageResource(data.imgThumb)
            tvPlaceName.text = data.tvPlaceName
            tvVote.text = data.tvVote
        }
    }

    override fun getItemCount(): Int = modelTrendingList.size


    class ViewHolder(val binding: ListItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root)
}