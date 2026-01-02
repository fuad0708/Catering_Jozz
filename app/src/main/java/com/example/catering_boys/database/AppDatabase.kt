package com.example.catering_boys.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 1. Version dinaikkan ke 2 karena UID berubah dari Int ke String.
 * 2. ExportSchema diset false untuk menyederhanakan build.
 */
@Database(entities = [DatabaseModel::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "catering_db"
                )
                    /**
                     * SOLUSI ANTI MENTAL:
                     * Baris ini akan menghapus database versi lama secara otomatis
                     * jika terjadi perubahan struktur tabel (skema).
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}