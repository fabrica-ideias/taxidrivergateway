package com.example.betho.taxidrivergateway

import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.text.format.Formatter
import android.widget.TextView
import java.net.InetAddress

/**
* Created by hugo on 14/09/17.
*/
class GetLocalHostTask(private val ip_gateway_campo : TextView, private val wm : WifiManager) : AsyncTask<Unit,Unit,String>() {
    //obtem o endereço de ip e converte para string através da manipulação direta da memória
    override fun doInBackground(vararg p0: Unit?): String? = String.format("%d.%d.%d.%d",
            wm.connectionInfo.ipAddress and 0xff,
            wm.connectionInfo.ipAddress shr 8 and 0xff,
            wm.connectionInfo.ipAddress shr 16 and 0xff,
            wm.connectionInfo.ipAddress shr 24 and 0xff)
    override fun onPostExecute(result: String?) {
        ip_gateway_campo.text = result
        super.onPostExecute(result)
    }
}