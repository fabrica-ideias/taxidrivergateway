package com.example.betho.taxidrivergateway

import android.content.Context
import android.os.AsyncTask
import android.os.Looper
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

/**
* Created by hugo on 04/09/17.
*/
class RequisitaRecurso(private val url: String, private val c: Context) : AsyncTask<Unit,Unit,Boolean>() {
    override fun doInBackground(vararg p0: Unit?): Boolean? {
        when (c) {
            is MainActivity -> {
                val url = URL(url)
                val urlConnection = url.openConnection() as HttpURLConnection
                return try {
                    BufferedInputStream(urlConnection.inputStream)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                } finally {
                    urlConnection.disconnect()
                }
            }
            is TesteActivity->
            {
                if(Looper.myLooper() == null)
                    Looper.prepare()
                val url = URL(url)
                val urlConnection = url.openConnection() as HttpURLConnection
                return try {
                    BufferedInputStream(urlConnection.inputStream)
                    true
                }catch (e: Exception)
                {
                    e.printStackTrace()
                    false
                }finally {
                    urlConnection.disconnect()
                }
            }
            else ->
            {
                return false
            }

        }
    }
    override fun onPostExecute(result: Boolean?) {
        when(c)
        {
            is TesteActivity->
            {
                if(result!!)
                {
                    c.toast(R.string.conexao_ok)
                }
                else
                {
                    c.longToast(R.string.conexao_error)
                }
            }
        }
        super.onPostExecute(result)
    }
}