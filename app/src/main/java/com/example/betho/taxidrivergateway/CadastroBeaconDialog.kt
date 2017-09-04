package com.example.betho.taxidrivergateway

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.cadastro_beacon_dialog_layout.*

/**
* Created by hugo on 04/09/17.
*/
class CadastroBeaconDialog(c: Context) : AlertDialog(c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_beacon_dialog_layout)
        finalizar_cadastro_beacon_btn.setOnClickListener { _ ->

        }
    }
}