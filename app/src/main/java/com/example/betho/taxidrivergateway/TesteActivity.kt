package com.example.betho.taxidrivergateway

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_teste.*
import org.jetbrains.anko.defaultSharedPreferences

class TesteActivity : AppCompatActivity() {
    private lateinit var prefs : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste)
        prefs = defaultSharedPreferences
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        teste_btn.setOnClickListener { _ ->
            val recurso = RequisitaRecurso("http://${prefs.getString("ip","")}/taxi/www/php",this@TesteActivity)
            recurso.execute()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
