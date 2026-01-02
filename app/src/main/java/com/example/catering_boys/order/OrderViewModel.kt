package com.example.catering_boys.order

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.catering_boys.database.DatabaseClient
import com.example.catering_boys.database.DatabaseModel
import com.example.catering_boys.database.DatabaseDao
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseDao: DatabaseDao = DatabaseClient.getInstance(application).appDatabase.databaseDao()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Orders")

    // Inisialisasi SharedPreferences untuk mengambil data user yang login
    private val sharedPref = application.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

    fun addDataOrder(strMenu: String, strJmlItems: Int, strHarga: Int, strPayment: String) {
        val orderId = dbRef.push().key ?: return

        // Ambil data USERNAME dan EMAIL yang disimpan saat Login/Register
        val currentUsername = sharedPref.getString("USERNAME", "Customer")
        val currentEmail = sharedPref.getString("EMAIL", "-")

        val databaseModel = DatabaseModel().apply {
            uid = orderId
            nama_menu = strMenu
            items = strJmlItems
            totalPrice = strHarga
            paymentMethod = strPayment
            status = "Pending"
            // MASUKKAN DATA USER DISINI AGAR MUNCUL DI FIREBASE
            username = currentUsername
            email = currentEmail
        }

        // Simpan ke Firebase (Sekarang sudah ada Nama & Email)
        dbRef.child(orderId).setValue(databaseModel)

        // Simpan ke Room Lokal
        Completable.fromAction {
            databaseDao.insertData(databaseModel)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    fun updateDataOrder(uid: String, strMenu: String, strJmlItems: Int, strHarga: Int, strPayment: String) {
        // Update di Firebase
        val updateMap = mapOf(
            "nama_menu" to strMenu,
            "items" to strJmlItems,
            "totalPrice" to strHarga,
            "paymentMethod" to strPayment
        )
        dbRef.child(uid).updateChildren(updateMap)

        // Update di Room Lokal
        Completable.fromAction {
            databaseDao.updateData(strMenu, strJmlItems, strHarga, strPayment, "Pending", uid)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}