package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.atualizar_cadastro_beacon_layout.*

/**
* Created by hugo on 19/09/17.
*/
class AtualizarCadastroBeaconDialog(private val c: Context, private val mac:String) : Dialog(c) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atualizar_cadastro_beacon_layout)
        setTitle(R.string.atualizar_beacon_title)
        Log.d("mac do dispositivo",mac)
        atualizar_cadastro_btn.setOnClickListener { _ ->
            val query = "UPDATE Beacon SET nome='${beacon_nome.text}' WHERE mac='$mac'"
            val acessoBD = AcessoBD(query,c,this,false)
            acessoBD.execute()
        }
    }
}