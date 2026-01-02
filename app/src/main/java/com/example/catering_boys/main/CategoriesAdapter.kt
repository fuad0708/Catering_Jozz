package com.example.catering_boys.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catering_boys.databinding.ListItemCategoriesBinding // Import binding untuk layout item
import com.example.catering_boys.order.OrderActivity

class CategoriesAdapter(
    private val ctx: Context,
    private val modelCategoriesList: List<ModelCategories>
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    // Menggunakan ListItemCategoriesBinding sebagai pengganti View manual
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemCategoriesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelCategoriesList[position]

        // Akses view melalui binding yang ada di holder
        holder.binding.apply {
            imageIcon.setImageResource(data.iIcon)
            tvName.text = data.strName

            cvCategories.setOnClickListener {
                val intent = Intent(ctx, OrderActivity::class.java)
                intent.putExtra(OrderActivity.DATA_TITLE, data.strName)
                ctx.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = modelCategoriesList.size

    // ViewHolder sekarang menerima parameter binding, bukan View
    class ViewHolder(val binding: ListItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root)
}