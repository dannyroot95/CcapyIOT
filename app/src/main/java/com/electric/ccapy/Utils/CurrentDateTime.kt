package com.electric.ccapy.Utils

class CurrentDateTime {

    fun getMonth(month : String ): String {

        var currentMonth = ""

        if (month == "01"){
            currentMonth = "Enero"
        }else if (month == "02"){
            currentMonth = "Febrero"
        }else if (month == "03"){
            currentMonth = "Marzo"
        }else if (month == "04"){
            currentMonth = "Abril"
        }else if (month == "05"){
            currentMonth = "Mayo"
        }else if (month == "06"){
            currentMonth = "Junio"
        }else if (month == "07"){
            currentMonth = "Julio"
        }else if (month == "08"){
            currentMonth = "Agosto"
        }else if (month == "09"){
            currentMonth = "Septiembre"
        }else if (month == "10"){
            currentMonth = "Octubre"
        }else if (month == "11"){
            currentMonth = "Noviembre"
        }else if (month == "12"){
            currentMonth = "Diciembre"
        }

        return  currentMonth
    }

}