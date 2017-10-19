package com.example.betho.taxidrivergateway

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ConfigActivity : AppCompatActivity() {
    private lateinit var prefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private lateinit var ip_banco : EditText
    private lateinit var banco_nome : EditText
    private lateinit var banco_login : EditText
    private lateinit var senha_banco : EditText
    private lateinit var ip_servidor : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputLayout = TextInputLayout(this@ConfigActivity)
        val inputLayout2 = TextInputLayout(this@ConfigActivity)
        val inputLayout3 = TextInputLayout(this@ConfigActivity)
        val inputLayout4 = TextInputLayout(this@ConfigActivity)
        val inputLayout5 = TextInputLayout(this@ConfigActivity)
        linearLayout {
            orientation = LinearLayout.VERTICAL
            ip_banco = editText {
                hintResource = R.string.ip_banco
            }
            ip_servidor = editText {
                hintResource = R.string.ip_servidor_app
            }
            banco_nome = editText {
                hintResource = R.string.banco_nome
            }
            removeView(ip_banco)
            removeView(ip_servidor)
            removeView(banco_nome)
            inputLayout.addView(ip_banco)
            inputLayout2.addView(ip_servidor)
            inputLayout3.addView(banco_nome)
            addView(inputLayout)
            addView(inputLayout2)
            addView(inputLayout3)
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                banco_login = editText {
                    hintResource = R.string.banco_login
                }.lparams{
                    weight = 1f
                    width = resources.displayMetrics.widthPixels/2
                }
                senha_banco = editText {
                    hintResource = R.string.banco_senha
                }.lparams{
                    weight = 1f
                    width = resources.displayMetrics.widthPixels/2
                }
                removeView(banco_login)
                removeView(senha_banco)
                inputLayout4.addView(banco_login)
                inputLayout5.addView(senha_banco)
                addView(inputLayout4)
                addView(inputLayout5)
            }.lparams {
                width = matchParent
            }
            button {
                textResource = R.string.ok_txt
                onClick {
                    ip_servidor.setText(prefs.getString("ip",""))
                    ip_servidor.setSelection(ip_servidor.text.toString().length)
                    editor.putString("ip_banco", ip_banco.text.toString())
                    editor.putString("banco_nome",banco_nome.text.toString())
                    editor.putString("ip",ip_servidor.text.toString())
                    editor.putString("banco_login",banco_login.text.toString())
                    editor.putString("senha_banco", senha_banco.text.toString())
                    editor.commit()
                    finish()
                }
            }.lparams{
                setMargins(dip(20),dip(50),dip(20),0)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        prefs = defaultSharedPreferences
        editor = prefs.edit()
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