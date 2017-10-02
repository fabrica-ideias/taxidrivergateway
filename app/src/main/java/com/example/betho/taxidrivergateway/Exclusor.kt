package com.example.betho.taxidrivergateway

import android.content.Context
import android.os.CountDownTimer

/**
* Created by hugo on 29/09/17.
*/
class Exclusor(intervalo: Long, tempofuturo: Long, private val c: Context, private val obj: String) : CountDownTimer(intervalo, tempofuturo) {
    override fun onFinish() {
        c as MainActivity
        c.delDetectado(obj)
    }

    override fun onTick(p0: Long) {

    }
}