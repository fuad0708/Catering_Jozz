package com.example.catering_boys.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.catering_boys.database.DatabaseClient
import com.example.catering_boys.database.DatabaseDao
import com.example.catering_boys.database.DatabaseModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseDao: DatabaseDao = DatabaseClient.getInstance(application).appDatabase.databaseDao()


    fun addDataRegister(strEmail: String, strUsername: String, strPassword: String) {
        Completable.fromAction {
            val databaseModel = DatabaseModel().apply {

                email = strEmail
                username = strUsername
                password = strPassword
            }
            databaseDao.insertData(databaseModel)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.d("RegisterViewModel", "Data user berhasil disimpan ke lokal")
            }, { throwable ->

                Log.e("RegisterViewModel", "Gagal menyimpan data: ${throwable.message}")
                throwable.printStackTrace()
            })
    }
}