package com.example.betho.taxidrivergateway

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_config.*
import org.jetbrains.anko.defaultSharedPreferences

class ConfigActivity : AppCompatActivity() {
    private lateinit var prefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        prefs = defaultSharedPreferences
        editor = prefs.edit()
        ip_servidor.setText(prefs.getString("ip",""))
        ip_servidor.setSelection(ip_servidor.text.toString().length)
        ok_btn.setOnClickListener { _ ->
            editor.putString("ip_banco", ip_banco.text.toString())
            editor.putString("banco_nome",banco_nome.text.toString())
            editor.putString("ip",ip_servidor.text.toString())
            editor.putString("banco_login",banco_login.text.toString())
            editor.putString("senha_banco", senha_banco.text.toString())
            editor.commit()
            finish()
        }
    }

    override fun onResume() {
        ip_servidor.setText(prefs.getString("ip",""))
        ip_servidor.setSelection(ip_servidor.text.toString().length)
        ip_banco.setText(prefs.getString("ip_banco",""))
        ip_banco.setSelection(ip_banco.text.toString().length)
        banco_nome.setText(prefs.getString("banco_nome",""))
        banco_nome.setSelection(banco_nome.text.toString().length)
        banco_login.setText(prefs.getString("banco_login",""))
        banco_login.setSelection(banco_login.text.toString().length)
        senha_banco.setText(prefs.getString("senha_banco",""))
        senha_banco.setSelection(senha_banco.text.toString().length)
        super.onResume()
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}