package com.example.betho.taxidrivergateway

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_scan_beacon.*
import org.jetbrains.anko.*
import java.util.*

class ScanBeaconActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var escaneando = true
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val view = p1 as TextView
        val texto = view.text.toString()
        when(p1.parent)
        {
            beacon_lista->
            {
                dialog = alert(R.string.det_beacon)
                {
                    customView {
                        linearLayout {
                            orientation = LinearLayout.VERTICAL
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                textView{
                                    setText(R.string.rssi_label)
                                    padding = dip(10)
                                }
                                textView{
                                    text = listarssi[texto].toString()
                                }
                            }
                            linearLayout {
                                orientation = LinearLayout.HORIZONTAL
                                textView {
                                    setText(R.string.distancia_label)
                                    padding = dip(10)
                                }
                                textView {
                                    text = distancia(listarssi[texto]!!)

                                }
                            }
                        }.applyRecursively { view -> when(view){
                            is TextView-> view.textSize = 20F
                        } }
                    }
                    okButton {
                        Log.d("dialog","ok btn")
                        dialog = null }
                }
                dialog?.show()
            }
        }
    }

    private var btadapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var scanner : BluetoothLeScanner
    private lateinit var lista_dispositivos : ArrayAdapter<String>
    private val listarssi = Hashtable<String,Int>()
    private var dialog: AlertBuilder<DialogInterface>? = null
    private val rssicalibrado = Hashtable<String,Double>()
    private val deteccoesrssibeacon = Hashtable<String,ArrayList<Int>>()
    private val calibragemRssi = { rssi : Int, mac: String-> //identifica valor adequado do rssi a 1 metro do beacon em específico
        try
        {
            deteccoesrssibeacon[mac]!!.add(rssi)
        }catch (e: Exception)
        {
            deteccoesrssibeacon.put(mac,ArrayList())
        }
        try {
            var soma = 0
            val rssimedio : Int
            if(deteccoesrssibeacon[mac]!!.size >= 20)
            {
                for(i in 0 until deteccoesrssibeacon.size)
                    soma+=deteccoesrssibeacon[mac]!![i]
                rssimedio = soma/deteccoesrssibeacon.size
                rssicalibrado.put(mac,rssimedio.toDouble())
            }
        }catch (e: KotlinNullPointerException)
        {
            e.printStackTrace()
        }catch (e: NullPointerException)
        {
            e.printStackTrace()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode)
        {
            Activity.RESULT_OK->
            {
                scanner = btadapter!!.bluetoothLeScanner
                scanner.startScan(callback)
            }
        }
    }
    private val distancia = { rssi : Int ->
        val rssiAtOneMetter = -47.0
        val distance = Math.pow(10.0,(rssiAtOneMetter - rssi)/20)
        distance.toString()
    }
    private val callback = object : ScanCallback()
    {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if(callbackType == ScanSettings.CALLBACK_TYPE_FIRST_MATCH || callbackType == ScanSettings.MATCH_MODE_AGGRESSIVE)
            {
                try {
                    if(lista_dispositivos.getPosition(result!!.device.address) == -1)
                    {
                        lista_dispositivos.add(result.device.address)
                    }
                    listarssi.put(result.device.address,result.rssi)

                }catch(e: Exception)
                {
                    e.printStackTrace()
                }
            }
            lista_dispositivos.notifyDataSetChanged()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_beacon)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar.isIndeterminate = true
        lista_dispositivos = ArrayAdapter(this@ScanBeaconActivity, R.layout.list_layout)
        beacon_lista.adapter = lista_dispositivos
        beacon_lista.onItemClickListener = this
        when
        {
            btadapter == null -> Toast.makeText(this@ScanBeaconActivity,R.string.bt_error, Toast.LENGTH_SHORT).show()
            btadapter?.isEnabled == false -> enableBT()
            else -> {
                scanner = btadapter!!.bluetoothLeScanner
                scanner.startScan(callback)
            }

        }
        scanner_controle_btn.setOnClickListener { view ->
            view as Button
            if(escaneando)
            {
                view.setText(R.string.iniciar_scanner)
                progressBar.visibility = View.INVISIBLE
                scanner.stopScan(callback)
            }
            else
            {
                view.setText(R.string.parar_scanner)
                progressBar.visibility = View.VISIBLE
                scanner.startScan(callback)
            }
            escaneando = !escaneando
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
    private val enableBT = {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent,1)
    }
}
