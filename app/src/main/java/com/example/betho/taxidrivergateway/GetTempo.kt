package com.example.betho.taxidrivergateway

import java.text.SimpleDateFormat
import java.util.*

/**
* Created by hugo on 26/09/17.
*/
class GetTempo {
    private val calendario = Calendar.getInstance()
    private val formatador = SimpleDateFormat("HH:mm:ss")
    val getTempo = {
        formatador.format(calendario.time)
    }
}