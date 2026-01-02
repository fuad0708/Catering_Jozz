package com.example.catering_boys.order

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.catering_boys.R
import com.example.catering_boys.utils.FunctionHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class OrderActivity : AppCompatActivity() {

    private var strTitle: String? = null
    private val hargaPaket = intArrayOf(10500, 34000, 23700, 22500, 16500, 26000)
    private val itemCount = IntArray(6) { 0 }
    private val countHarga = IntArray(6) { 0 }

    private var totalItems = 0
    private var totalPrice = 0

    // Menggunakan String UID agar sinkron dengan Room & Firebase
    private var isUpdate = false
    private var orderUid: String = ""

    private lateinit var toolbar: Toolbar
    private lateinit var tvJumlahPorsi: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: MaterialButton
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Ambil data dari Intent
        isUpdate = intent.getBooleanExtra("IS_UPDATE", false)
        orderUid = intent.getStringExtra("UID") ?: ""

        setStatusbar()
        setInitLayout()
        setupAllPaket()
        setInputData()
    }

    private fun setInitLayout() {
        val tvPaket11 = findViewById<TextView>(R.id.tvPaket11)
        toolbar = findViewById(R.id.toolbar)
        tvJumlahPorsi = findViewById(R.id.tvJumlahPorsi)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnCheckout = findViewById(R.id.btnCheckout)

        intent.extras?.let {
            strTitle = it.getString(DATA_TITLE)
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = if (isUpdate) "Update $strTitle" else strTitle
            }
        }

        // Efek coret pada harga diskon
        tvPaket11.paintFlags = tvPaket11.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
    }

    private fun setupAllPaket() {
        val addIds = intArrayOf(R.id.imageAdd1, R.id.imageAdd2, R.id.imageAdd3, R.id.imageAdd4, R.id.imageAdd5, R.id.imageAdd6)
        val minusIds = intArrayOf(R.id.imageMinus1, R.id.imageMinus2, R.id.imageMinus3, R.id.imageMinus4, R.id.imageMinus5, R.id.imageMinus6)
        val textIds = intArrayOf(R.id.tvPaket1, R.id.tvPaket2, R.id.tvPaket3, R.id.tvPaket4, R.id.tvPaket5, R.id.tvPaket6)

        for (i in 0..5) {
            val tvPaket = findViewById<TextView>(textIds[i])

            findViewById<ImageView>(addIds[i]).setOnClickListener {
                itemCount[i]++
                tvPaket.text = itemCount[i].toString()
                countHarga[i] = hargaPaket[i] * itemCount[i]
                calculateTotal()
            }

            findViewById<ImageView>(minusIds[i]).setOnClickListener {
                if (itemCount[i] > 0) {
                    itemCount[i]--
                    tvPaket.text = itemCount[i].toString()
                    countHarga[i] = hargaPaket[i] * itemCount[i]
                    calculateTotal()
                }
            }
        }
    }

    private fun calculateTotal() {
        totalItems = itemCount.sum()
        totalPrice = countHarga.sum()

        tvJumlahPorsi.text = "$totalItems items"
        tvTotalPrice.text = FunctionHelper.rupiahFormat(totalPrice)
    }

    private fun setInputData() {
        btnCheckout.setOnClickListener {
            when {
                totalItems == 0 || totalPrice == 0 -> {
                    Toast.makeText(this, "Ups, pilih menu makanan dulu!", Toast.LENGTH_SHORT).show()
                }
                totalItems < 10 -> {
                    Toast.makeText(this, "Ups, minimal 10 pesanan!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    showPaymentDialog()
                }
            }
        }
    }

    private fun showPaymentDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_payment_method, null)
        dialog.setContentView(view)

        val btnKonfirmasi = view.findViewById<MaterialButton>(R.id.btnKonfirmasiBayar)
        val rgPayment = view.findViewById<RadioGroup>(R.id.rgPaymentBottomSheet)

        btnKonfirmasi.setOnClickListener {
            val selectedId = rgPayment.checkedRadioButtonId
            val rbSelected = view.findViewById<RadioButton>(selectedId)
            val paymentMethod = rbSelected?.text.toString() ?: "Bayar di Tempat (COD)"

            if (isUpdate) {
                // Update data pesanan yang sudah ada
                orderViewModel.updateDataOrder(orderUid, strTitle!!, totalItems, totalPrice, paymentMethod)
                Toast.makeText(this, "Pesanan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            } else {
                // Simpan pesanan baru
                orderViewModel.addDataOrder(strTitle!!, totalItems, totalPrice, paymentMethod)
                Toast.makeText(this, "Yeay! Pesanan baru diproses!", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_TITLE = "TITLE"
    }
}