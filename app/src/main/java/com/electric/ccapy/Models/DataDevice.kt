package com.electric.ccapy.Models

data class DataDevice(var ampere : Float = 0f,
                      var chip_id : Int = 0,
                      var energy : Float = 0f,
                      var frequency : Float = 0f,
                      var power_factor : Float = 0f,
                      var time : Long = 0L,
                      var voltage : Float = 0f,
                      var watts : Float = 0f)
