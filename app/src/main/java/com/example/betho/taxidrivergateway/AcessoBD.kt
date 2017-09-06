package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/**
* Created by hugo on 04/09/17.
*/
class AcessoBD(private val query: String, private val c: Context, private val dialog : Dialog?, private val actionFlag : Boolean) : AsyncTask<Unit,Unit,ResultSet>() {
    private val entries = ArrayList<String>()
    private val user = "eswwe2adzte58aou"
    private val pass = "wp3f4xc9yboetlll"
    private val url = "jdbc:mysql://ffn96u87j5ogvehy.cbetxkdyhwsb.us-east-1.rds.amazonaws.com/znhi6rzef19zuaml"
    override fun doInBackground(vararg p0: Unit?): ResultSet? {
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        var resultSet : ResultSet? = null
        try
        {
            val con = DriverManager.getConnection(url,user,pass)
            val smt = con.createStatement()
            if(actionFlag)
                resultSet = smt.executeQuery(query)
            else
                smt.execute(query)
        }catch(e: SQLException)
        {
            e.printStackTrace()
        }
        return resultSet
    }

    override fun onPostExecute(result: ResultSet?) {
        super.onPostExecute(result)
        when(c)
        {
            is CadastroBeaconActivity->
            {
                if(actionFlag)
                {
                    val arrayAdapter = ArrayAdapter<String>(c, R.layout.list_layout)
                    arrayAdapter.add("0 - Nenhum")
                    try {
                        while(result!!.next())
                            arrayAdapter.add("${result.getString(1)} - ${result.getString(4)}")
                    }catch(e : SQLException)
                    {
                        e.printStackTrace()
                    }
                    dialog?.lista_carros?.adapter = arrayAdapter
                    Log.d("lista carros",arrayAdapter.toString())
                }
                else
                {
                    dialog?.dismiss()
                }
            }
        }
    }
}