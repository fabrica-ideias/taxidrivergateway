package com.example.betho.taxidrivergateway

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager
import kotlinx.android.synthetic.main.content_main.*

/**
* Created by hugo on 12/09/17.
*/
class EstadoDispositivo(private val c : Context) : Runnable {
    override fun run() {
        when(c)
        {
            is MainActivity->
            {
                val estadorede = getEstadoRede()
                if(estadorede != ConnectivityManager.TYPE_MOBILE && estadorede != ConnectivityManager.TYPE_WIFI)
                {
                    c.aviso.setText(R.string.sem_rede_aviso)
                }
            }
        }
    }

    val getNivelBateria = {
        try {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val bstatus = c.registerReceiver(null, ifilter)
            val nivel = bstatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val escala = bstatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val prcBateria = nivel/escala.toFloat() * 100
            prcBateria
        }catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    val getEstadoRede = {
        val rede = RedeReceiver()
        rede.getRedeAtiva()
    }
}