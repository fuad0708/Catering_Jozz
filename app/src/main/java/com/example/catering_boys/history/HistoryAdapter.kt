package com.example.catering_boys.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.catering_boys.R
import com.example.catering_boys.database.DatabaseModel
import com.example.catering_boys.utils.FunctionHelper
import com.google.android.material.button.MaterialButton

class HistoryAdapter(
    private val mContext: Context,
    private var modelDatabase: MutableList<DatabaseModel>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // Interface untuk menangani klik tombol "Pesan Lagi"
    interface OnHistoryClickListener {
        fun onReorder(data: DatabaseModel)
    }

    private var listener: OnHistoryClickListener? = null

    // Fungsi untuk memasang listener dari Activity
    fun setOnHistoryClickListener(listener: OnHistoryClickListener) {
        this.listener = listener
    }

    // Memperbarui data adapter secara keseluruhan
    fun setDataAdapter(items: List<DatabaseModel>) {
        modelDatabase.clear()
        modelDatabase.addAll(items)
        notifyDataSetChanged()
    }

    // Mengambil data yang ada di adapter
    fun getData(): MutableList<DatabaseModel> {
        return modelDatabase
    }

    // Fungsi untuk menghapus item (dipakai saat swipe delete)
    fun setSwipeRemove(position: Int) {
        if (position >= 0 && position < modelDatabase.size) {
            modelDatabase.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Mengembalikan item yang dihapus jika user melakukan "Undo"
    fun restoreItem(model: DatabaseModel, position: Int) {
        modelDatabase.add(position, model)
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelDatabase[position]

        // Menampilkan data ke komponen UI
        holder.tvNamaMenu.text = data.nama_menu ?: "Menu Tidak Diketahui"
        holder.tvJmlItems.text = "${data.items} items"
        holder.tvTotalPrice.text = FunctionHelper.rupiahFormat(data.totalPrice)
        holder.tvStatus.text = data.status

        // Listener untuk tombol Pesan Lagi
        holder.btnPesanLagi.setOnClickListener {
            listener?.onReorder(data)
        }
    }

    override fun getItemCount(): Int {
        return modelDatabase.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaMenu: TextView = itemView.findViewById(R.id.tvNamaMenu)
        val tvJmlItems: TextView = itemView.findViewById(R.id.tvJmlItems)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnPesanLagi: MaterialButton = itemView.findViewById(R.id.btnPesanLagi)
    }
}