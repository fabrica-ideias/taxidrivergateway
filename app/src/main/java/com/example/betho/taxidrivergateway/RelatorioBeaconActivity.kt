package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_relatorio_beacon.*
import org.jetbrains.anko.toast

class RelatorioBeaconActivity : AppCompatActivity() {
    private lateinit var sqlite : AcessoSQLite
    private lateinit var adapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorio_beacon)
        adapter = ArrayAdapter(this@RelatorioBeaconActivity,R.layout.list_layout)
        sqlite = AcessoSQLite(this@RelatorioBeaconActivity)
        val cursor = sqlite.readableDatabase.rawQuery("SELECT * FROM Beacon",null)
        try {
            while(cursor!!.moveToNext())
            {
                adapter.add(cursor.getString(1))
            }
            lista_cadastrados.adapter = adapter
            cursor.close()
        }catch (e: KotlinNullPointerException)
        {
            toast(R.string.sqlite_error)
            finish()
        }
    }
}
