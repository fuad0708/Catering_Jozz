package com.example.catering_boys.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catering_boys.database.DatabaseModel
import com.example.catering_boys.databinding.ItemStatusPesananBinding // Import binding class
import com.example.catering_boys.utils.FunctionHelper

class StatusPesananAdapter(private val list: List<DatabaseModel>) :
    RecyclerView.Adapter<StatusPesananAdapter.ViewHolder>() {

    // ViewHolder sekarang menerima parameter binding, bukan View manual
    class ViewHolder(val binding: ItemStatusPesananBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate menggunakan binding class
        val binding = ItemStatusPesananBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        // Gunakan holder.binding untuk mengakses komponen UI
        holder.binding.apply {
            tvMenuName.text = data.nama_menu ?: "Menu Tidak Diketahui"

            val formatHarga = FunctionHelper.rupiahFormat(data.totalPrice)
            tvOrderDetails.text = "${data.items} Porsi - $formatHarga"

            tvPaymentMethod.text = "Bayar: ${data.paymentMethod ?: "COD"}"
            tvStatus.text = data.status ?: "Pending"

            // Contoh tambahan: Kamu bisa memberi warna berbeda pada status secara dinamis
            if (data.status == "Selesai") {
                tvStatus.setTextColor(root.context.getColor(android.R.color.holo_green_dark))
            }
        }
    }

    override fun getItemCount(): Int = list.size
}