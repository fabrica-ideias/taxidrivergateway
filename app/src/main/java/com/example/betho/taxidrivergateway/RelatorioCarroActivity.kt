package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_relatorio_carro.*
import org.jetbrains.anko.db.asSequence
import org.jetbrains.anko.db.select

class RelatorioCarroActivity : AppCompatActivity() {
    private lateinit var carros_cadastrados : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio_carro)
        carros_cadastrados = ArrayAdapter(this@RelatorioCarroActivity, R.layout.list_layout)
        AcessoSQLite(this@RelatorioCarroActivity).readableDatabase.select("Carro").exec {
            this.asSequence().forEachIndexed {index,  obj ->
                carros_cadastrados.add(obj[index].toString())
            }
        }
        lista_carros.adapter = carros_cadastrados
    }
}
