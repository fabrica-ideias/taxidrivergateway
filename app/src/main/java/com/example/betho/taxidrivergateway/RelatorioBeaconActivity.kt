package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_relatorio_beacon.*
import org.jetbrains.anko.*

class RelatorioBeaconActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        alert(R.string.det_beacon) {
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        textView {
                            setText(R.string.carro_cadastrado_label)
                            text = "$text "
                        }
                        textView {
                            text = ""
                        }
                    }
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        textView {
                            setText(R.string.deteccoes_beacon_label)
                            text = "$text "
                        }
                        textView {
                            text = ""
                        }
                    }
                }.applyRecursively { view ->
                    when(view)
                    {
                        is TextView ->
                        {
                            view.padding = dip(10)
                            view.textSize = 20f
                        }

                    }
                }
            }
            okButton {  }
        }.show()
    }

    private lateinit var sqlite : AcessoSQLite
    private lateinit var adapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio_beacon)
        adapter = ArrayAdapter(this@RelatorioBeaconActivity,R.layout.list_layout)
        sqlite = AcessoSQLite(this@RelatorioBeaconActivity)
        //val cursor = sqlite.readableDatabase.rawQuery("SELECT * FROM Beacon INNER JOIN ON Carro.fk_beacon=Beacon.mac",null)
        try {
            val query = "SELECT * FROM Beacon INNER JOIN Carro ON Carro.fk_beacon=Beacon.mac";
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
}
