package com.example.betho.taxidrivergateway

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
* Created by hugo on 12/09/17.
*/
class AcessoSQLite(c: Context) : ManagedSQLiteOpenHelper(c,"beacons",null,1) {
    private val NOME = "nome"
    private val RSSI = "rssi"
    private val DISTANCIA = "distancia"
    private val MAC = "mac"
    private var db : SQLiteDatabase? = null
    companion object {
        private var instance: AcessoSQLite? = null

        @Synchronized
        fun getInstance(c: Context): AcessoSQLite {
            if (instance == null) {
                instance = AcessoSQLite(c.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable("Beacon", true,  MAC to TEXT + PRIMARY_KEY,NOME to TEXT, RSSI to TEXT, DISTANCIA to REAL)
        db = p0
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable("Beacon")
        this.onCreate(p0)
    }
    val Context.database: AcessoSQLite
        get() = AcessoSQLite.getInstance(applicationContext)
}