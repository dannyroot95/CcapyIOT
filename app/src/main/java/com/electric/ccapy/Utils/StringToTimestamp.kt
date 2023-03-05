package com.electric.ccapy.Utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StringToTimestamp {

    @SuppressLint("SimpleDateFormat")
    fun value(srt : String) : Long{
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
        val date: Date = formatter.parse(srt) as Date
        return date.time/1000
    }

}