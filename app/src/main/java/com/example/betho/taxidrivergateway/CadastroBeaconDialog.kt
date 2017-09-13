package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*

/**
* Created by hugo on 04/09/17.
*/
class CadastroBeaconDialog(private val c: Context, private val mac : String) : Dialog(c) {
    private var query = ""
    private val cadastroBeaconActivity = c as CadastroBeaconActivity
    private val distancia = { rssi : Int ->
        val rssiAtOneMetter = -38.0
        val distance = Math.pow(10.0,(rssiAtOneMetter - rssi)/20)
        distance.toString()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_beacon_dialog_layout)
        query = "SELECT * FROM Carro"
        var acessoBD = AcessoBD(query, c, this,true)
        acessoBD.execute()
        finalizar_cadastro_beacon_btn.setOnClickListener { _ ->
            if(lista_carros.selectedItem.toString().split(" - ")[0] == "0")
                query = "INSERT INTO Beacon VALUES ('$mac','${cadastroBeaconActivity.getRssi(mac)}',${distancia(cadastroBeaconActivity.getRssi(mac)!!.toInt())},'${beacon_nome.text}')"
            else
                query = "INSERT INTO Beacon VALUES (LAST_INSERT_ID(),'${beacon_nome.text}','${cadastroBeaconActivity.getRssi(mac)}',${distancia(cadastroBeaconActivity.getRssi(mac)!!.toInt())},'$mac');UPDATE Carro SET()"
            acessoBD = AcessoBD(query, c, this, false)
            acessoBD.execute()
        }
    }
}