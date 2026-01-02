package com.example.catering_boys.utils

import android.text.format.DateFormat
import java.text.DecimalFormat
import java.util.Calendar

object FunctionHelper {


    fun rupiahFormat(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return "Rp " + formatter.format(price.toLong()).replace(",", ".")
    }


    fun getToday(): String {
        val date = Calendar.getInstance().time
        return DateFormat.format("d MMMM yyyy", date).toString()
    }
}