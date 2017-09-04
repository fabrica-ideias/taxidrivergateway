package com.example.betho.taxidrivergateway

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro_beacon.*

class CadastroBeaconActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val view = p1 as TextView
        val cadastroBeaconDialog = CadastroBeaconDialog(this@CadastroBeaconActivity)
        when(p1.parent)
        {
            listview_dispositivos->
            {
                val mac_dispositivo = view.text.toString()
                cadastroBeaconDialog.show()
            }
        }
    }

    private var bt_adapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var scanner : BluetoothLeScanner
    private lateinit var lista_dispositivos : ArrayAdapter<String>
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode)
        {
            Activity.RESULT_OK->
            {
                scanner = bt_adapter!!.bluetoothLeScanner
                scanner.startScan(callback)
            }
        }
    }
    private val addDispotivo = { dispositivoNome: String ->
        if(lista_dispositivos.getPosition(dispositivoNome) == -1)
            lista_dispositivos.add(dispositivoNome)
    }
    private val callback = object : ScanCallback()
    {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if(callbackType == ScanSettings.CALLBACK_TYPE_FIRST_MATCH || callbackType == ScanSettings.MATCH_MODE_AGGRESSIVE)
            {
                try {
                    addDispotivo(result!!.device.address)
                    Log.d("modo padrão","detectado")
                }catch(e: Exception)
                {
                    Log.d("exceção","modo padrão")
                }
            }
            lista_dispositivos.notifyDataSetChanged()
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_beacon)
        lista_dispositivos = ArrayAdapter(this@CadastroBeaconActivity,R.layout.list_layout)
        listview_dispositivos.adapter = lista_dispositivos
        listview_dispositivos.onItemClickListener = this
        when
        {
            bt_adapter == null -> Toast.makeText(this@CadastroBeaconActivity,R.string.bt_error,Toast.LENGTH_SHORT).show()
            bt_adapter?.isEnabled == false -> enableBT()
            else -> {
                scanner = bt_adapter!!.bluetoothLeScanner
                scanner.startScan(callback)
            }

        }
    }
    private val enableBT = {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent,1)
    }

    override fun onBackPressed() {
        scanner.stopScan(callback)
        super.onBackPressed()
    }
}
