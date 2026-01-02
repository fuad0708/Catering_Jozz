package com.example.catering_boys.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.catering_boys.databinding.ActivityLoginBinding
import com.example.catering_boys.main.MainActivity
import com.example.catering_boys.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
        setInputData()
    }

    private fun setInitLayout() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setInputData() {
        // Pindah ke halaman Daftar
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val strUsername = binding.inputUser.text.toString().trim()
            val strPassword = binding.inputPassword.text.toString().trim()

            if (strUsername.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(this, "Ups, Form harus diisi semua!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Memanggil data user dari Room Database
            loginViewModel.getDataUser(strUsername, strPassword).observe(this) { modelDatabases ->
                try {
                    if (modelDatabases != null && modelDatabases.isNotEmpty()) {
                        val user = modelDatabases[0]

                        // Simpan data ke SharedPreferences
                        val sharedPref = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()

                        // Simpan UID sebagai String (Penting agar sinkron dengan Firebase)
                        editor.putString("UID", user.uid)
                        editor.putString("USERNAME", user.username)
                        editor.putString("EMAIL", user.email ?: "email@cateringboys.com")
                        editor.apply()

                        Toast.makeText(this, "Selamat Datang, ${user.username}!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Jika data tidak ditemukan (misal karena belum daftar ulang)
                        Toast.makeText(this, "Akun tidak ditemukan. Silakan Register ulang!", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    // Mencegah aplikasi mental jika terjadi error tak terduga
                    Log.e("LoginActivity", "Error Login: ${e.message}")
                    Toast.makeText(this, "Terjadi kesalahan sistem.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}