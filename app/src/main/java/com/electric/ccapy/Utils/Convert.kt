package com.electric.ccapy.Utils

import kotlin.math.roundToInt

class Convert {

    fun twoDecimals(value : Float) : String{

        var data = value
        data = ((value * 100.0).roundToInt() / 100.00).toFloat()
        return data.toString()

    }

}