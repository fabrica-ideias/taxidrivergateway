package com.example.betho.taxidrivergateway

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_config.*
import org.jetbrains.anko.defaultSharedPreferences

class ConfigActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var prefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        when(p0)
        {
            sensibilidade->
            {
                sensibilidade_valor.text = p1.toString()
            }
            latencia->
            {
                latencia_valor.text = p1.toString()
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        prefs = defaultSharedPreferences
        editor = prefs.edit()
        sensibilidade.max = 10
        latencia.max = 10
        sensibilidade.setOnSeekBarChangeListener(this)
        latencia.setOnSeekBarChangeListener(this)
        ok_btn.setOnClickListener { _ ->
            editor.putInt("sensibilidade_valor",sensibilidade_valor.text.toString().toInt())
            editor.putInt("latencia_valor",latencia_valor.text.toString().toInt())
            editor.commit()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}