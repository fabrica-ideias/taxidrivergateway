package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
* Created by hugo on 04/09/17.
*/
class CadastroBeaconDialog(private val c: Context, private val mac : String) : Dialog(c) {
    private var query : String = ""
    private val cadastroBeaconActivity = c as CadastroBeaconActivity
    private val distancia = { rssi : Int ->
        val rssiAtOneMetter = -38.0
        val distance = Math.pow(10.0,(rssiAtOneMetter - rssi)/20)
        distance
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_beacon_dialog_layout)
        query = "SELECT * FROM Carro"
        var acessoBD = AcessoBD(query, c, this,true)
        acessoBD.execute()
        finalizar_cadastro_beacon_btn.setOnClickListener { _ ->
            val carro = lista_carros.selectedItem.toString()
            if(carro.split("-")[0] == "0")
            {
                query = "INSERT INTO Beacon VALUES ('$mac','${cadastroBeaconActivity.getRssi(mac)}',${distancia(cadastroBeaconActivity.getRssi(mac)!!.toInt()).toInt()},'${beacon_nome.text}',' ',0)"
                acessoBD = AcessoBD(query, c, this@CadastroBeaconDialog, false)
                acessoBD.execute()
            }
            else
            {
                //query = "INSERT INTO Beacon VALUES (LAST_INSERT_ID(),'${beacon_nome.text}','${cadastroBeaconActivity.getRssi(mac)}',${distancia(cadastroBeaconActivity.getRssi(mac)!!.toInt())},'$mac');"
                query = "INSERT INTO Beacon VALUES ('$mac','${cadastroBeaconActivity.getRssi(mac)}',${distancia(cadastroBeaconActivity.getRssi(mac)!!.toInt()).toShort()},'${beacon_nome.text}',' ',0);"
                val query2 = "UPDATE Carro SET fk_beacon=(SELECT MAX(mac) FROM Beacon WHERE carroid='$carro');"
                c.alert(R.string.calibrar_aviso){
                    yesButton {
                        acessoBD = AcessoBD(query, c, this@CadastroBeaconDialog, false)
                        acessoBD.execute()
                        doAsync {
                            while(acessoBD.status == AsyncTask.Status.RUNNING)
                            {
                                Log.d("status task",acessoBD.status.toString())
                            }
                            acessoBD = AcessoBD(query2, c, this@CadastroBeaconDialog, false)
                            acessoBD.execute()
                        }
                    }
                    noButton {  }
                }.show()
            }
        }
    }
}