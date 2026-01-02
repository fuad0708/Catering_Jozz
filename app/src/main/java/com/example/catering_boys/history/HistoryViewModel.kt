package com.example.catering_boys.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.catering_boys.database.DatabaseClient
import com.example.catering_boys.database.DatabaseDao
import com.example.catering_boys.database.DatabaseModel
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseDao: DatabaseDao = DatabaseClient.getInstance(application).appDatabase.databaseDao()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Orders")

    /**
     * PERBAIKAN: Memfilter data agar hanya menampilkan yang sudah melakukan order.
     * Data register yang harganya 0 atau nama_menu null tidak akan muncul.
     */
    fun getDataList(): LiveData<List<DatabaseModel>> {
        return databaseDao.getAllOrder().map { list ->
            list.filter { it.totalPrice > 0 && it.nama_menu != null }
        }
    }

    /**
     * Menghapus data berdasarkan UID di Firebase dan Room.
     */
    fun deleteDataById(uid: String) {
        // 1. Hapus di Firebase
        dbRef.child(uid).removeValue()

        // 2. Hapus di Room Lokal
        Completable.fromAction {
            databaseDao.deleteSingleData(uid)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Berhasil dihapus
            }, {
                it.printStackTrace()
            })
    }
}