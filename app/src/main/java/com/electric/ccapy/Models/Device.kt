package com.electric.ccapy.Models

import java.sql.Timestamp

data class Device (
                    val id_chip : String = "",
                    val id_user : String = "",
                    val fullname : String = "",
                    val date_synchronize: Long = 0L
                    )
