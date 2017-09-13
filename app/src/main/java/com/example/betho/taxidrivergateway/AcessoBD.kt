package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.net.UnknownHostException
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/**
* Created by hugo on 04/09/17.
*/
class AcessoBD(private val query: String, private val c: Context, private val dialog : Dialog?, private val actionFlag : Boolean) : AsyncTask<Unit,Unit,ResultSet>() {
    private val user = "jedkdtu6vb5oo5o3"
    private val pass = "ps7auabtr43dftzs"
    private val url = "jdbc:mysql://ysp9sse09kl0tzxj.cbetxkdyhwsb.us-east-1.rds.amazonaws.com/du31xu75psg7waby"
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
        }catch (e: UnknownHostException)
        {
            e.printStackTrace()
            c.longToast(R.string.sem_rede_aviso)
        }catch (e: SQLException)
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
                    }catch (e: KotlinNullPointerException)
                    {
                        c.toast("Não há carros cadastrados")
                    }
                    dialog?.lista_carros?.adapter = arrayAdapter
                    Log.d("lista carros",arrayAdapter.toString())
                }
                else
                {
                    dialog?.dismiss()
                    c.alert(R.string.sucesso_msg)
                    {
                        yesButton { c.finish() }
                    }.show()

                }
            }
            is MainActivity->
            {
                if(actionFlag)
                {
                    try {
                        val sqlite = AcessoSQLite(c)
                        while(result!!.next())
                        {
                            sqlite.use { insert("Beacon", "mac" to result.getString(1), "nome" to result.getString(4), "rssi" to result.getString(2), "distancia" to result.getString(3)) }
                        }
                    }catch (e: SQLException)
                    {
                        c.toast("Erro sqlite")
                        e.printStackTrace()
                    }
                    catch(e: KotlinNullPointerException)
                    {
                        Log.d("resultado","nulo")
                        e.printStackTrace()
                    }
                }
                else
                {

                }
            }
        }
    }
}