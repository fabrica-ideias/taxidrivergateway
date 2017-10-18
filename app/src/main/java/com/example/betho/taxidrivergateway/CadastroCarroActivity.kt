package com.example.betho.taxidrivergateway

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import com.example.hugo.lessapedidos.GetScreenMetrics
import kotlinx.android.synthetic.main.activity_cadastro_carro.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class CadastroCarroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_cadastro_carro)
        val inputLayout1 = TextInputLayout(this@CadastroCarroActivity)
        val inputLayout2 = TextInputLayout(this@CadastroCarroActivity)
        linearLayout {
            orientation = LinearLayout.VERTICAL
            textView {
                textResource = R.string.cadastro_carro_menu_txt
                textSize = GetScreenMetrics(resources.displayMetrics.density).getFontSizeDialogTitle()
            }.lparams{
                setMargins(0,dip(10),0,dip(50))
                gravity = Gravity.CENTER_HORIZONTAL
            }
            val nome_carro = editText {
                hintResource = R.string.carro_nome_hint
            }
            val placa_carro = editText{
                hintResource = R.string.placa_carro_hint
                addTextChangedListener(object : TextWatcher{
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
            }
            removeView(nome_carro)
            removeView(placa_carro)
            inputLayout1.addView(nome_carro)
            inputLayout2.addView(placa_carro)
            addView(inputLayout1)
            addView(inputLayout2)
            button {
                textResource = R.string.cadastro_carro_btn
                onClick {
                    if(nome_carro.text.toString().trim() == "" || placa_carro.text.toString().trim() == "")
                        toast(R.string.validacao_cadastro_msg)
                    else
                    {
                        val placa = placa_carro.text.toString()
                        val nome = nome_carro.text.toString()
                        AcessoBD("INSERT INTO Carro VALUES('$placa',NULL,2,3,'$nome')",this@CadastroCarroActivity,null,false).execute()
                    }
                }
            }.lparams{
                setMargins(dip(20),dip(140),dip(20),0)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }.applyRecursively { view ->
            when(view)
            {
                is TextInputLayout->
                        view.padding = dip(12)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
