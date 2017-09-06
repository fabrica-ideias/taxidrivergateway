package com.example.betho.taxidrivergateway

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.sucesso_dialog_layout.*

/**
* Created by hugo on 06/09/17.
*/
class SucessoDialog(private val c : Context) : AlertDialog(c) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sucesso_dialog_layout)
        ok_btn.setOnClickListener { _ ->
            val aux = c as Activity
            aux.finish()
            dismiss()
        }
    }
}