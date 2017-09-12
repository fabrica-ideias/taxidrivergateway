package com.example.betho.taxidrivergateway

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
* Created by hugo on 12/09/17.
*/
class RedeReceiver : BroadcastReceiver() {
    private var redeAtiva : Int = ConnectivityManager.TYPE_WIFI
    val getRedeAtiva = {
        redeAtiva
    }
    override fun onReceive(p0: Context?, p1: Intent?) {
        val cm = p0?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val redeAtiva = cm.activeNetworkInfo
        when(redeAtiva.type)
        {
            ConnectivityManager.TYPE_WIFI->
                    this.redeAtiva = ConnectivityManager.TYPE_WIFI
            ConnectivityManager.TYPE_MOBILE->
                    this.redeAtiva = ConnectivityManager.TYPE_MOBILE
        }
    }
}