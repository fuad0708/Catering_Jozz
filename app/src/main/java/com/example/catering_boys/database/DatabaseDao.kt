package com.example.catering_boys.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {

    // 1. Mengambil semua data pesanan tanpa filter UID
    @Query("SELECT * FROM tbl_catering")
    fun getAllOrder(): LiveData<List<DatabaseModel>>

    // 2. Fungsi Login: Mencocokkan username dan password
    @Query("SELECT * FROM tbl_catering WHERE username = :username AND password = :password")
    fun getUserByName(username: String, password: String): LiveData<List<DatabaseModel>>

    // 3. Insert data: Menggunakan OnConflictStrategy.REPLACE agar jika ID sama data akan diupdate
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(vararg modelDatabases: DatabaseModel)

    // 4. FUNGSI UPDATE PESANAN: Parameter uid menggunakan String
    @Query("""
        UPDATE tbl_catering 
        SET nama_menu = :nama_menu, 
            jml_items = :jml_items, 
            total_price = :totalPrice,
            payment_method = :paymentMethod,
            status = :status
        WHERE uid = :uid
    """)
    fun updateData(
        nama_menu: String,
        jml_items: Int,
        totalPrice: Int,
        paymentMethod: String,
        status: String,
        uid: String // Pastikan String
    )

    // 5. FUNGSI UPDATE STATUS: Digunakan oleh Penjual untuk mengubah status via String UID
    @Query("UPDATE tbl_catering SET status = :status WHERE uid = :uid")
    fun updateStatusPesanan(status: String, uid: String)

    // 6. Hapus data berdasarkan UID String (Penyebab error di HistoryActivity sebelumnya)
    @Query("DELETE FROM tbl_catering WHERE uid = :uid")
    fun deleteSingleData(uid: String)
}