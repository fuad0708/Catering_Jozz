package com.example.catering_boys.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {


    @Query("SELECT * FROM tbl_catering")
    fun getAllOrder(): LiveData<List<DatabaseModel>>


    @Query("SELECT * FROM tbl_catering WHERE username = :username AND password = :password")
    fun getUserByName(username: String, password: String): LiveData<List<DatabaseModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(vararg modelDatabases: DatabaseModel)


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
        uid: String
    )


    @Query("UPDATE tbl_catering SET status = :status WHERE uid = :uid")
    fun updateStatusPesanan(status: String, uid: String)


    @Query("DELETE FROM tbl_catering WHERE uid = :uid")
    fun deleteSingleData(uid: String)
}