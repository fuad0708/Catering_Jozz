package com.example.catering_boys.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.catering_boys.R
import com.example.catering_boys.address.AlamatActivity // Import Activity Alamat yang baru
import com.example.catering_boys.databinding.ActivityAccountBinding
import com.example.catering_boys.history.HistoryOrderActivity
import com.example.catering_boys.login.LoginActivity

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding untuk akses komponen UI
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil data user dari SharedPreferences
        val sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val username = sharedPref.getString("USERNAME", "User Catering")
        val email = sharedPref.getString("EMAIL", "email@cateringboys.com")

        // Menampilkan data user ke TextView
        binding.tvUsername.text = username
        binding.tvEmail.text = email

        // Navigasi ke Riwayat Pesanan
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryOrderActivity::class.java)
            startActivity(intent)
        }

        // PERBAIKAN: Sekarang membuka halaman Google Maps (AlamatActivity)
        binding.btnAddress.setOnClickListener {
            val intent = Intent(this, AlamatActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke Dashboard Penjual
        binding.btnSwitchMode.setOnClickListener {
            val intent = Intent(this, SellerDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Logika Logout
        binding.btnLogout.setOnClickListener {
            // Menghapus sesi login
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            // Kembali ke halaman Login dan hapus tumpukan activity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show()
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_account
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, SellerDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_account -> true
                else -> false
            }
        }
    }
}