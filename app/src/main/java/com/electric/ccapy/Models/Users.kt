package com.electric.ccapy.Models

data class Users(var fullname : String = "" ,
                 var email : String = "" ,
                 var phone : String = "",
                 var dni : String = "",
                 var status_account : Int = 0,
                 var type : String = "",
                 var hasDevice : Boolean = false,
                 var current_device_id : String = "",
                 var id : String = "")
