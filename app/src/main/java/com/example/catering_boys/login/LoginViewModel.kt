package com.example.catering_boys.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.catering_boys.database.DatabaseClient
import com.example.catering_boys.database.DatabaseDao
import com.example.catering_boys.database.DatabaseModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    // Inisialisasi DAO dari DatabaseClient
    private val databaseDao: DatabaseDao = DatabaseClient.getInstance(application).appDatabase.databaseDao()

    /**
     * Fungsi untuk mengambil data user berdasarkan username dan password.
     * Mengembalikan LiveData agar Activity dapat mengobservasi hasilnya.
     */
    fun getDataUser(username: String, password: String): LiveData<List<DatabaseModel>> {
        return databaseDao.getUserByName(username, password)
    }
}