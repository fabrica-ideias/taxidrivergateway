package com.example.betho.taxidrivergateway

import android.content.Context
import android.os.AsyncTask
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
                val url = URL(url)
                var urlConnection = url.openConnection() as HttpURLConnection
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
                return false
        }
    }
    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }
}