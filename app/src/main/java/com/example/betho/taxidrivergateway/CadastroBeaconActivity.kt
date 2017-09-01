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
import android.widget.ArrayAdapter
import android.widget.Toast

class CadastroBeaconActivity : AppCompatActivity() {
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
            }
        }
    }
    private val callback = object : ScanCallback()
    {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            when(callbackType)
            {
                ScanSettings.CALLBACK_TYPE_FIRST_MATCH->
                {

                }
                ScanSettings.MATCH_MODE_AGGRESSIVE->
                {

                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_beacon)
        lista_dispositivos = ArrayAdapter(this@CadastroBeaconActivity,R.layout.list_layout)
        when
        {
            bt_adapter == null -> Toast.makeText(this@CadastroBeaconActivity,R.string.bt_error,Toast.LENGTH_SHORT).show()
            bt_adapter?.isEnabled == false -> enableBT()
            else -> scanner = bt_adapter!!.bluetoothLeScanner
        }
    }
    private val enableBT = {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent,1)
    }
}
