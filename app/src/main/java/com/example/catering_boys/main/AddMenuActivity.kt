package com.example.catering_boys.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.catering_boys.databinding.ActivityAddMenuBinding // Import binding class

class AddMenuActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setInitAction()
    }

    private fun setInitAction() {

        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.btnSimpanMenu.setOnClickListener {

            val nama = binding.etNamaMenu.text.toString().trim()
            val harga = binding.etHargaMenu.text.toString().trim()

            if (nama.isNotEmpty() && harga.isNotEmpty()) {

                Toast.makeText(this, "Menu $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()



                finish()
            } else {
                Toast.makeText(this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}