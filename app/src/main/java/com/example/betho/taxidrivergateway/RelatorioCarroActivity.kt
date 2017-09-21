package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_relatorio_carro.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.asSequence
import org.jetbrains.anko.db.select

class RelatorioCarroActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p1 as TextView
        val placa = p1.text.toString()
        when(p1.parent)
        {
            lista_carros->
            {
                alert(R.string.carro_detalhes_title){
                    customView {
                        scrollView {
                            horizontalScrollView {
                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    linearLayout {
                                        orientation = LinearLayout.HORIZONTAL
                                        textView {
                                            setText(R.string.nome_carro_label)
                                            sqlite.use { select("Carro").whereArgs("carroid={placa}", "placa" to placa).exec {
                                                while(this.moveToNext())
                                                {
                                                    text = "$text ${this.getString(4)}"
                                                }
                                            } }
                                        }
                                    }
                                    linearLayout {
                                        orientation = LinearLayout.HORIZONTAL
                                        textView {
                                            setText(R.string.beacon_associado_label)
                                            sqlite.use { select("Carro").whereArgs("carroid={placa}", "placa" to placa).exec {
                                                while(this.moveToNext())
                                                {
                                                    text = "$text ${this.getString(1)}"
                                                }
                                            } }
                                        }
                                    }
                                }
                            }
                        }.applyRecursively { view ->
                            when(view)
                            {
                                is TextView->
                                {
                                    view.textSize = 15f
                                    view.padding = dip(8)
                                }
                            }
                        }
                    }
                    okButton {  }
                }.show()
            }
        }
    }

    private lateinit var carros_cadastrados : ArrayAdapter<String>
    private val sqlite = AcessoSQLite(this@RelatorioCarroActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio_carro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        carros_cadastrados = ArrayAdapter(this@RelatorioCarroActivity, R.layout.list_layout)
        AcessoSQLite(this@RelatorioCarroActivity).readableDatabase.select("Carro").exec {
            this.asSequence().forEachIndexed {index,  obj ->
                carros_cadastrados.add(obj[index].toString())
            }
        }
        lista_carros.adapter = carros_cadastrados
        lista_carros.setOnItemClickListener(this)
    }

     override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         finish()
         return super.onOptionsItemSelected(item)
    }
}
