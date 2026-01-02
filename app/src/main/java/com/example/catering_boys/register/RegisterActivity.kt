package com.example.catering_boys.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.catering_boys.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
        setInputData()
    }

    private fun setInitLayout() {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private fun setInputData() {
        binding.btnRegister.setOnClickListener {
            val strEmail = binding.inputEmail.text.toString().trim()
            val strUser = binding.inputUser.text.toString().trim()
            val strPassword = binding.inputPassword.text.toString().trim()

            if (strEmail.isEmpty() || strUser.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(this, "Ups, Form harus diisi semua!", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Jalankan proses simpan
                registerViewModel.addDataRegister(strEmail, strUser, strPassword)

                // 2. Beri jeda atau beri tahu user
                Toast.makeText(this, "Pendaftaran sedang diproses...", Toast.LENGTH_SHORT).show()

                // 3. Tambahkan sedikit delay agar RxJava selesai menulis ke Room
                binding.btnRegister.postDelayed({
                    Toast.makeText(this, "Berhasil! Silahkan Login.", Toast.LENGTH_SHORT).show()
                    finish()
                }, 500)
            }
        }
    }
}