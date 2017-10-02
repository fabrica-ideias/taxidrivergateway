package com.example.betho.taxidrivergateway

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import java.net.UnknownHostException
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

/**
* Created by hugo on 04/09/17.
*/
class AcessoBD(private val query: String, private val c: Context, private val dialog : Dialog?, private val actionFlag : Boolean) : AsyncTask<Unit,Unit,ResultSet>() {
    private val prefs = c.defaultSharedPreferences
    //private val user = "jedkdtu6vb5oo5o3"
    private val user = "root"
    //private val pass = "ps7auabtr43dftzs"
    private val pass = "fi2108"
    //private val url = "jdbc:mysql://ysp9sse09kl0tzxj.cbetxkdyhwsb.us-east-1.rds.amazonaws.com/du31xu75psg7waby"
    private val url = "jdbc:mysql://186.213.56.92/du31xu75psg7waby"
    private val progressDialog = c.indeterminateProgressDialog(message = R.string.progresso_msg, title = R.string.progresso_tittle)
    private val dialogSucesso = {
        val aux = c as Activity
        c.alert(R.string.sucesso_msg)
        {
            yesButton { aux.finish() }
        }.show()
    }
    override fun onPreExecute() {
        progressDialog.show()
        super.onPreExecute()
    }
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
        progressDialog.dismiss()
        super.onPostExecute(result)
        when(c)
        {
            is CadastroBeaconActivity->
            {
                if(actionFlag)
                {
                    val arrayAdapter = ArrayAdapter<String>(c, R.layout.list_layout)
                    arrayAdapter.add("0-Nenhum")
                    try {
                        while(result!!.next())
                            arrayAdapter.add(result.getString(1))
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
                    dialogSucesso()
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
                            try
                            {
                                result.findColumn("mac")
                                sqlite.use { insert("Beacon", "mac" to result.getString(1), "nome" to result.getString(4), "rssi" to result.getString(2), "distancia" to result.getString(3), "ultimadeteccao" to result.getString(6), "passagens" to result.getInt(5)) }
                                c.setMacsUltimaDeteccao(result.getString(1),result.getString(6))
                                c.setBeaconMacs(result.getString(1))
                            }catch (e: SQLException)
                            {
                                sqlite.readableDatabase.select("Carro").exec {
                                    sqlite.use {
                                        insert("Carro", "carroid" to result.getString(1),"fk_beacon" to result.getString(2), "fk_situacao" to result.getString(3), "fk_posto" to result.getString(4), "nome" to result.getString(5))
                                        insert("Situacao", "situacaoid" to result.getInt(3), "nome" to result.getString(5))
                                        insert("Posto", "postoid" to result.getInt(4), "nome" to result.getString(9))
                                    }
                                }
                            }
                        }
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
            is CadastroCarroActivity->
            {
                if(actionFlag)
                {

                }
                else
                {
                    dialogSucesso()
                }
            }
            is RelatorioBeaconActivity->
            {
                try {
                    val mac_passagens = Hashtable<String,Int>()
                    val mac_nome = Hashtable<String,String>()
                    val mac_carro = Hashtable<String,String>()
                    val mac_ultimadeteccao = Hashtable<String,String>()
                    while(result!!.next())
                    {
                        c.getAdapter().add(result.getString(1))
                        mac_passagens.put(result.getString(1),result.getInt(6))
                        mac_nome.put(result.getString(1),result.getString(4))
                        try
                        {
                            mac_carro.put(result.getString(1),result.getString(7))
                        }catch (e: NullPointerException)
                        {
                            mac_carro.put(result.getString(1),"Nenhum")
                        }
                        try {
                            mac_ultimadeteccao.put(result.getString(1),result.getString(5))
                        }catch (e: NullPointerException)
                        {
                            mac_ultimadeteccao.put(result.getString(1),"Nenhum")
                        }

                    }
                    c.getAdapter().notifyDataSetChanged()
                    c.setPassagens(mac_passagens)
                    c.setNomes(mac_nome)
                    c.setCarros(mac_carro)
                    c.setUltimaDeteccao(mac_ultimadeteccao)
                }catch (e: SQLException)
                {

                }catch (e: KotlinNullPointerException)
                {

                }
            }
        }
    }
}