package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastro_carro.*
import org.jetbrains.anko.toast

class CadastroCarroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_carro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        placa_carro.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.length == 3 && p3>p2)
                {
                    placa_carro.setText("${placa_carro.text}-")
                    placa_carro.setSelection(placa_carro.length())
                }
            }

        })
        finalizar_cadastro_carro_btn.setOnClickListener { _ ->
            if(nome_carro.text.toString().trim() == "" || placa_carro.text.toString().trim() == "")
                toast(R.string.validacao_cadastro_msg)
            else
            {
                val placa = placa_carro.text.toString()
                val nome = nome_carro.text.toString()
                AcessoBD("INSERT INTO Carro VALUES('$placa',NULL,2,3,'$nome')",this@CadastroCarroActivity,null,false).execute()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
