package com.example.catering_boys.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_catering")
data class DatabaseModel(
    // 1. Ubah PrimaryKey menjadi String dan hapus autoGenerate
    @PrimaryKey
    var uid: String = "",

    @ColumnInfo(name = "nama_menu")
    var nama_menu: String? = null,

    @ColumnInfo(name = "jml_items")
    var items: Int = 0,

    @ColumnInfo(name = "total_price")
    var totalPrice: Int = 0,

    @ColumnInfo(name = "payment_method")
    var paymentMethod: String? = "COD",

    @ColumnInfo(name = "status")
    var status: String? = "Pending",

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null
) : Serializable {
    // 2. Tambahkan Constructor Kosong wajib untuk Firebase Realtime Database
    constructor() : this("", null, 0, 0, "COD", "Pending", null, null, null)
}