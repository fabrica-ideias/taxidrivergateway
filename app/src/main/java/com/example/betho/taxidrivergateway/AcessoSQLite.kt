package com.example.betho.taxidrivergateway

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
* Created by hugo on 12/09/17.
*/
class AcessoSQLite(c: Context) : ManagedSQLiteOpenHelper(c,"beacons",null,1) {
    //atributos de ambos
    private val NOME = "nome"
    //atributos de beacon
    private val RSSI = "rssi"
    private val DISTANCIA = "distancia"
    private val MAC = "mac"
    private val ULTIMA_DETECCAO = "ultimadeteccao"
    private val PASSAGENS = "passagens"
    //atributos de carro
    private val CARROID = "carroid"
    private val FKBEACON = "fk_beacon"
    private val FKSITUACAO = "fk_situacao"
    private val FKPOSTO = "fk_posto"
    private lateinit var db : SQLiteDatabase
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

    override fun onCreate(p0: SQLiteDatabase) {
        p0.createTable("Beacon", true,  MAC to TEXT + PRIMARY_KEY,NOME to TEXT, RSSI to TEXT, DISTANCIA to REAL, ULTIMA_DETECCAO to TEXT, PASSAGENS to INTEGER)
        p0.createTable("Carro",true, CARROID to TEXT + PRIMARY_KEY, FKBEACON to TEXT, FKSITUACAO to INTEGER, FKPOSTO to INTEGER, NOME to TEXT)
        db = p0
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.dropTable("Beacon")
        p0.dropTable("Carro")
        this.onCreate(p0)
    }
    val Context.database: AcessoSQLite
        get() = AcessoSQLite.getInstance(applicationContext)
}