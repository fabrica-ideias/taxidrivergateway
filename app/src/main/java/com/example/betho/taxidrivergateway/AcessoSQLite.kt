package com.example.betho.taxidrivergateway

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
* Created by hugo on 08/09/17.
*/
class AcessoSQLite(c : Context, dbname: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(c, dbname, factory, version){
    private val ID = "beaconid"
    private val NOME = "nome"
    private val RSSI = "rssi"
    private val DISTANCIA = "distancia"
    private val MAC = "mac"
    private var db : SQLiteDatabase? = null
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE IF NOT EXISTS Beacon ($ID integer primary key autoincrement, $NOME text, $RSSI text, $DISTANCIA integer, $MAC text)")
        db = p0
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS Beacon")
        onCreate(p0)
    }

    val select = { query : String ->
        val res = db?.rawQuery(query,null)
        db?.close()
        res
    }

    val insert = { query : String ->
        val res = db?.rawQuery(query,null)
        db?.close()
        res
    }
}