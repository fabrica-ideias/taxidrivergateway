package com.example.betho.taxidrivergateway

import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.ArraySet
import java.sql.DriverManager
import java.sql.SQLException

/**
* Created by hugo on 04/09/17.
*/
class AcessoBD(private val query: String) : AsyncTask<Unit,Unit,Unit>() {
    private val url = "jdbc:mysql://localhost/taxidriver"
    private val user = ""
    private val pass = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun doInBackground(vararg p0: Unit?) {
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        try
        {
            val con = DriverManager.getConnection(url, user, pass)
            val smt = con.createStatement()
            val resultSet = smt.executeQuery(query)
            val entries = ArraySet<String>()
            while(resultSet.next())
            {
                entries.add(resultSet.getString(1))
            }
        }catch(e: SQLException)
        {
            e.printStackTrace()
        }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

    }
}