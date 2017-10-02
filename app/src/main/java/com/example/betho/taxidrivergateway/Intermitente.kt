package com.example.betho.taxidrivergateway

import android.content.Context
import android.os.Looper
import java.util.*

/**
* Created by hugo on 18/09/17.
*/
class Intermitente(private val c: Context, private val tempo: Long) : TimerTask() {
    override fun run() {
      when(c)
      {
          is MainActivity->
          {
              if(Looper.myLooper() == null)
                Looper.prepare()
              val aux = c.getDetectados()
              try {
                  for(i in 0 until aux.size)
                  {
                      //c.notificaDeteccao(aux[i])
                      Exclusor(tempo, tempo, c, aux[i]).start()
                  }
              }catch (e: IndexOutOfBoundsException)
              {
                  e.printStackTrace()
              }
          }
      }
    }
}