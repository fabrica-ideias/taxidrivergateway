package com.example.betho.taxidrivergateway

import android.content.Context
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
* Created by hugo on 18/09/17.
*/
class Intermitente(private val mac : String, contador: Int, private val c: Context) : TimerTask() {
    private val url = URL("http://192.168.7.115/taxidrivercall/php/intermitente.php?mac=$mac&contador=$contador")
    override fun run() {
        val request = url.openConnection() as HttpURLConnection
        try {
            BufferedInputStream(request.inputStream)
            val aux = c as MainActivity
            aux.setFlagEnvioServer(mac)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}