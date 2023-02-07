package com.electric.ccapy.Models

data class Config(
    var actual_read : String = "",
    var actual_med_read: String = "",
    val limit : String = "",
    val day_invoice : String = "",
    val notify_limit : Boolean = false,
    val notify_intelligent : Boolean = false,
    val is_reset : String = "",
    val counter : Int = 0
)
