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
    private val PROXIMA_DETECCAO = "proximadeteccao"
    //atributos de carro
    private val CARROID = "carroid"
    private val FKBEACON = "fk_beacon"
    private val FKSITUACAO = "fk_situacao"
    private val FKPOSTO = "fk_posto"
    //atributos de situação
    private val SITUACAOID = "situacaoid"
    //atributos de posto
    private val POSTOID = "postoid"
    private lateinit var db : SQLiteDatabase

    override fun onCreate(p0: SQLiteDatabase) {
        p0.createTable("Beacon", true,  MAC to TEXT + PRIMARY_KEY,NOME to TEXT, RSSI to TEXT, DISTANCIA to REAL, ULTIMA_DETECCAO to TEXT, PASSAGENS to INTEGER)
        p0.createTable("Carro",true, CARROID to TEXT + PRIMARY_KEY, FKBEACON to TEXT, FKSITUACAO to INTEGER, FKPOSTO to INTEGER, NOME to TEXT)
        p0.createTable("Situacao",true, SITUACAOID to TEXT + PRIMARY_KEY, NOME to TEXT)
        p0.createTable("Posto",true, POSTOID to TEXT + PRIMARY_KEY, NOME to TEXT)
        db = p0
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.dropTable("Beacon")
        p0.dropTable("Carro")
        p0.dropTable("Situacao")
        p0.dropTable("Posto")
        this.onCreate(p0)
    }
}