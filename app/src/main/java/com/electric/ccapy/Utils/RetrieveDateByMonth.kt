package com.electric.ccapy.Utils

import android.app.Activity
import com.electric.ccapy.Models.Config
import com.electric.ccapy.Providers.InvoiceByDateProvider
import com.electric.ccapy.UI.InvoiceActivity
import com.electric.ccapy.databinding.ActivityInvoiceBinding
import java.time.Month
import java.util.*

class RetrieveDateByMonth {

    val months = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    fun getMonth(activity: Activity,month: String,binding: ActivityInvoiceBinding,year:String){

        val config = TinyDB(activity).getObject(Constants.CONFIG, Config::class.java)!!
        val cacheDay = config.day_invoice.split("/")[0].toInt()
        var rt = ""

        if(month == months[0]){
            rt = "01/01/$year 00:01,$cacheDay/01/$year 23:59"
        }else if(month == months[1]){
            rt = "01/02/$year 00:01,$cacheDay/02/$year 23:59"
        }else if(month == months[2]){
            rt = "01/03/$year 00:01,$cacheDay/03/$year 23:59"
        }else if(month == months[3]){
            rt = "01/04/$year 00:01,$cacheDay/04/$year 23:59"
        }else if(month == months[4]){
            rt = "01/05/$year 00:01,$cacheDay/05/$year 23:59"
        }else if(month == months[5]){
            rt = "01/06/$year 00:01,$cacheDay/06/$year 23:59"
        }else if(month == months[6]){
            rt = "01/07/$year 00:01,$cacheDay/07/$year 23:59"
        }else if(month == months[7]){
            rt = "01/08/$year 00:01,$cacheDay/08/$year 23:59"
        }else if(month == months[8]){
            rt = "01/09/$year 00:01,$cacheDay/09/$year 23:59"
        }else if(month == months[9]){
            rt = "01/10/$year 00:01,$cacheDay/10/$year 23:59"
        }else if(month == months[10]){
            rt = "01/11/$year 00:01,$cacheDay/11/$year 23:59"
        }else{
            rt = "01/12/$year 00:01,$cacheDay/12/$year 23:59"
        }

        InvoiceByDateProvider().receiveMonths(rt,activity,binding,month,year)

    }
}