package com.example.betho.taxidrivergateway

import android.content.Context
import java.util.*

/**
* Created by hugo on 18/09/17.
*/
class Intermitente(private val c: Context) : TimerTask() {
    override fun run() {
      when(c)
      {
          is MainActivity->
          {
              val aux = c.getDetectados()
              try {
                  for(i in 0 until aux.size)
                  {
                      c.notificaDeteccao(aux[i])
                      aux.remove(aux[i])
                      c.setDetectados(aux)
                  }
              }catch (e: IndexOutOfBoundsException)
              {
                  e.printStackTrace()
              }
          }
      }
    }
}