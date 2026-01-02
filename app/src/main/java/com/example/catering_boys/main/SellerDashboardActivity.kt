package com.example.catering_boys.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catering_boys.R
import com.example.catering_boys.databinding.ActivitySellerDashboardBinding
import com.example.catering_boys.database.DatabaseModel
import com.example.catering_boys.utils.FunctionHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SellerDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerDashboardBinding
    private lateinit var adapter: StatusPesananAdapter

    // Referensi ke Firebase
    private val dbRef = FirebaseDatabase.getInstance().getReference("Orders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
        loadDataPesananFirebase() // Ganti ke fungsi Firebase
        setupBottomNavigation()
    }

    private fun setInitLayout() {
        binding.rvStatusPesanan.apply {
            layoutManager = LinearLayoutManager(this@SellerDashboardActivity)
            setHasFixedSize(true)
        }

        binding.btnTambahMenu.setOnClickListener {
            startActivity(Intent(this, AddMenuActivity::class.java))
        }
    }

    private fun loadDataPesananFirebase() {
        // Mendengarkan perubahan data secara Real-time dari Cloud
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOrder = mutableListOf<DatabaseModel>()
                var totalHarga = 0

                for (data in snapshot.children) {
                    val order = data.getValue(DatabaseModel::class.java)
                    if (order != null) {
                        listOrder.add(order)
                        totalHarga += order.totalPrice
                    }
                }

                // Update Statistik Ringkas
                binding.tvOrderCount.text = listOrder.size.toString()
                binding.tvTotalRevenue.text = FunctionHelper.rupiahFormat(totalHarga)

                // Jika data kosong tampilkan teks (Opsional, sesuaikan ID XML jika ada)
                if (listOrder.isEmpty()) {
                    binding.rvStatusPesanan.visibility = View.GONE
                } else {
                    binding.rvStatusPesanan.visibility = View.VISIBLE
                    // Kirim ke Adapter
                    adapter = StatusPesananAdapter(listOrder)
                    binding.rvStatusPesanan.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Logika jika gagal ambil data dari Firebase
            }
        })
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_dashboard
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_dashboard -> true
                R.id.nav_account -> {
                    startActivity(Intent(this, AccountActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}