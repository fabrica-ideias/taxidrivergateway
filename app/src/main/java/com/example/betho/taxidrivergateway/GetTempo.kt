package com.example.betho.taxidrivergateway

import java.text.SimpleDateFormat
import java.util.*

/**
* Created by hugo on 26/09/17.
*/
class GetTempo {
//    private val calendario = Calendar.getInstance()
    private val formatador = SimpleDateFormat("HH:mm:ss", Locale.CANADA)
    val getTempo = {
        formatador.format(Date().time)
    }
    val getTempoPorDate = { date: Date ->
        formatador.format(date.time)
    }
    val getTempoDiff = { data1: Date, data2: Date ->
        data2.time - data1.time
    }
}