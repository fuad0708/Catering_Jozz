package com.example.catering_boys.history

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catering_boys.databinding.ActivityHistoryOrderBinding
import com.example.catering_boys.database.DatabaseModel
import com.example.catering_boys.order.OrderActivity
import java.util.ArrayList

class HistoryOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryOrderBinding
    private var modelDatabaseList: MutableList<DatabaseModel> = ArrayList()
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding untuk akses UI
        binding = ActivityHistoryOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        setInitLayout()
        setViewModel()
        setSwipeToDelete()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setInitLayout() {
        binding.tvNotFound.visibility = View.GONE

        historyAdapter = HistoryAdapter(this, modelDatabaseList)

        // Logika ketika item riwayat diklik untuk reorder/update
        historyAdapter.setOnHistoryClickListener(object : HistoryAdapter.OnHistoryClickListener {
            override fun onReorder(data: DatabaseModel) {
                val intent = Intent(this@HistoryOrderActivity, OrderActivity::class.java)
                intent.putExtra("IS_UPDATE", true)
                // UID sekarang adalah String dari Firebase/Room
                intent.putExtra("UID", data.uid)
                intent.putExtra(OrderActivity.DATA_TITLE, data.nama_menu)
                startActivity(intent)
            }
        })

        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryOrderActivity)
            adapter = historyAdapter
        }
    }

    private fun setViewModel() {
        // Inisialisasi ViewModel
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Mengamati perubahan data dari Room/Firebase
        historyViewModel.getDataList().observe(this) { modelDatabases ->
            if (modelDatabases != null && modelDatabases.isNotEmpty()) {
                binding.tvNotFound.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
                historyAdapter.setDataAdapter(modelDatabases)
            } else {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            }
        }
    }

    private fun setSwipeToDelete() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val databaseModel = historyAdapter.getData()[position]

                AlertDialog.Builder(this@HistoryOrderActivity).apply {
                    setMessage("Hapus riwayat ini?")
                    setPositiveButton("Ya, Hapus") { _, _ ->
                        // UID dikirim sebagai String ke ViewModel
                        val uid = databaseModel.uid
                        historyViewModel.deleteDataById(uid)
                        historyAdapter.setSwipeRemove(position)
                        Toast.makeText(this@HistoryOrderActivity, "Data sudah dihapus", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Batal") { dialog, _ ->
                        // Kembalikan item jika batal hapus
                        historyAdapter.restoreItem(databaseModel, position)
                        binding.rvHistory.scrollToPosition(position)
                        dialog.cancel()
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rvHistory)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}