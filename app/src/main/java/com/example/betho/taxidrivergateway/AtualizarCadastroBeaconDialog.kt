package com.example.betho.taxidrivergateway

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.atualizar_cadastro_beacon_layout.*
import org.jetbrains.anko.db.select

/**
* Created by hugo on 19/09/17.
*/
class AtualizarCadastroBeaconDialog(private val c: Context) : Dialog(c), AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p1?.parent)
        {

        }
    }

    private lateinit var lista_carros : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atualizar_cadastro_beacon_layout)
        setTitle(R.string.atualizar_beacon_title)
        lista_carros = ArrayAdapter(c,R.layout.list_layout)
        AcessoSQLite(c).readableDatabase.select("Carro").exec {
            while(this.moveToNext())
            {
                lista_carros.add(this.getString(5))
            }
        }

        atualizar_cadastro_btn.setOnClickListener { _ ->
            lista_carro.adapter = lista_carros
        }
    }
}