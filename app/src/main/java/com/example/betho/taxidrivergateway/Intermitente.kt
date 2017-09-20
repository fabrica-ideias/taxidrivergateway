package com.example.betho.taxidrivergateway

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
* Created by hugo on 18/09/17.
*/
class Intermitente(mac : String) : TimerTask() {
    private val url = URL("http://taxidrivercall.000webhostapp.com/php/intermitente.php?mac=$mac")
    override fun run() {
        val request = url.openConnection() as HttpURLConnection
        try {
            BufferedInputStream(request.inputStream)
        }catch (e: Exception)
        {

        }
    }
}