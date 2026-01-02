package com.example.catering_boys.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.catering_boys.database.DatabaseClient
import com.example.catering_boys.database.DatabaseDao
import com.example.catering_boys.database.DatabaseModel

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private val databaseDao: DatabaseDao = DatabaseClient.getInstance(application).appDatabase.databaseDao()


    fun getDataUser(username: String, password: String): LiveData<List<DatabaseModel>> {
        return databaseDao.getUserByName(username, password)
    }
}