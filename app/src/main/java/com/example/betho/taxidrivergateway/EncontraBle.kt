package com.example.betho.taxidrivergateway

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.widget.ArrayAdapter

/**
* Created by hugo on 08/09/17.
*/
class EncontraBle {
    private var bt_adapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var scanner : BluetoothLeScanner
    private lateinit var lista_dispositivos : ArrayAdapter<String>

    val getBtAdapter = {
        bt_adapter
    }

    val getBtScanner = {
        scanner
    }

    val iniciaScan = { callback : ScanCallback ->
        getBtScanner().startScan(callback)
    }

    val paraScan = { callback : ScanCallback ->
        getBtScanner().stopScan(callback)
    }
}