package com.example.catering_boys.database

import android.content.Context

class DatabaseClient private constructor(context: Context) {

    // PERBAIKAN: Ambil instance dari AppDatabase.getInstance agar sinkron
    val appDatabase: AppDatabase = AppDatabase.getInstance(context)

    companion object {
        private var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(context)
            }
            return mInstance!!
        }
    }
}