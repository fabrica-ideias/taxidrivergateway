package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.atualizar_cadastro_beacon_layout.*

/**
* Created by hugo on 19/09/17.
*/
class AtualizarCadastroBeaconDialog(c: Context) : Dialog(c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atualizar_cadastro_beacon_layout)
        setTitle(R.string.atualizar_beacon_title)
        atualizar_cadastro_btn.setOnClickListener { _ ->

        }
    }
}