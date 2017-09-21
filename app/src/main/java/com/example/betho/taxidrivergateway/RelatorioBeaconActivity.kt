package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_relatorio_beacon.*
import org.jetbrains.anko.*
import java.util.*

class RelatorioBeaconActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val view = p1 as TextView
        val texto = view.text.toString()
        alert(R.string.det_beacon) {
            customView {
                scrollView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            textView {
                                setText(R.string.carro_cadastrado_label)
                                text = "$text ${mac_carro[texto]}"
                            }
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            textView {
                                setText(R.string.deteccoes_beacon_label)
                                text = "$text ${mac_passagens[texto]}"
                            }
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            textView {
                                setText(R.string.nome_beacon_label)
                                text = "$text ${mac_nome[texto]}"
                            }
                        }
                        linearLayout {
                            orientation = LinearLayout.HORIZONTAL
                            textView {
                                setText(R.string.ultima_deteccao_beacon)
                                text = "$text ${mac_ultimadeteccao[texto]}"
                            }
                        }
                    }.applyRecursively { view -> //aplica propriedades recursivamente às views passadas como parâmetro aqui
                        when(view)
                        {
                            is TextView ->
                            {
                                view.textSize = 15f
                            }
                            is LinearLayout->
                            {
                                if(view.orientation == LinearLayout.VERTICAL)
                                    view.padding = dip(20)
                                else
                                    view.padding = dip(5)
                            }


                        }
                    }
                }

            }
            okButton {  }
        }.show()
    }

    private lateinit var sqlite : AcessoSQLite
    private lateinit var adapter : ArrayAdapter<String>
    private var mac_passagens = Hashtable<String,Int>()
    private var mac_nome = Hashtable<String,String>()
    private var mac_carro = Hashtable<String,String>()
    private var mac_ultimadeteccao = Hashtable<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio_beacon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        adapter = ArrayAdapter(this@RelatorioBeaconActivity,R.layout.list_layout)
        sqlite = AcessoSQLite(this@RelatorioBeaconActivity)
        //val cursor = sqlite.readableDatabase.rawQuery("SELECT * FROM Beacon INNER JOIN ON Carro.fk_beacon=Beacon.mac",null)
        try {
            val query = "SELECT * FROM Beacon LEFT JOIN Carro ON Carro.fk_beacon=Beacon.mac"
            val acessoDB = AcessoBD(query, this@RelatorioBeaconActivity, null, true)
            acessoDB.execute()
            lista_cadastrados.adapter = adapter
            lista_cadastrados.onItemClickListener = this
        }catch (e: KotlinNullPointerException)
        {
            toast(R.string.sqlite_error)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
    val getAdapter = {
        adapter
    }
    val setPassagens = { passagens : Hashtable<String,Int> ->
        this.mac_passagens = passagens
    }
    val setNomes = { nomes : Hashtable<String,String> ->
        this.mac_nome = nomes
    }
    val setCarros = { carros: Hashtable<String,String> ->
        this.mac_carro = carros
    }
    val setUltimaDeteccao = { deteccoes : Hashtable<String,String> ->
        this.mac_ultimadeteccao = deteccoes
    }
}
