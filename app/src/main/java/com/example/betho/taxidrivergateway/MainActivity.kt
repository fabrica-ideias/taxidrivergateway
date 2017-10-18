package com.example.betho.taxidrivergateway

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.select
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    inner class Conttask: TimerTask() {
        private var cont = 0
        override fun run() {
            cont++
        }
        val getCont = {cont}
    }
    private var bt_adapter : BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var scanner : BluetoothLeScanner
    private lateinit var acessoBD : AcessoBD
    private lateinit var acessoDB2 : AcessoBD
    private var sqlite = AcessoSQLite(this@MainActivity)
    //private lateinit var intermitente : Intermitente
    private val rssi_calibrado = Hashtable<String,Double>()
    private val deteccoes_rssi_beacon = Hashtable<String,ArrayList<Int>>()
    private val flags_envia_servidor = Hashtable<String,Boolean>()
    private val beacon_macs = ArrayList<String>()
    private lateinit var contask: Conttask
    private lateinit var prefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var tempo_latencia = 0
    private val mac_ultimaDeteccao = Hashtable<String,String>()
    private val detectados = ArrayList<String>()
    //private val tempo = GetTempo()
    private var sensibilidade_distancia = 0
    private val enableBT = {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(intent,1)
    }
    private val calibragemRssi = { rssi : Int, mac: String-> //identifica valor adequado do rssi a 1 metro do beacon em específico
        try
        {
            deteccoes_rssi_beacon[mac]!!.add(rssi)
        }catch (e: Exception)
        {
            deteccoes_rssi_beacon.put(mac,ArrayList())
        }
        try {
            var soma = 0
            val rssi_medio : Int
            if(deteccoes_rssi_beacon[mac]!!.size >= 20)
            {
                for(i in 0 until deteccoes_rssi_beacon.size)
                    soma+=deteccoes_rssi_beacon[mac]!![i]
                rssi_medio = soma/deteccoes_rssi_beacon.size
                rssi_calibrado.put(mac,rssi_medio.toDouble())
            }
        }catch (e: KotlinNullPointerException)
        {
            e.printStackTrace()
        }catch (e: NullPointerException)
        {
            e.printStackTrace()
        }

    }
    val setFlagEnvioServer = { mac:String ->
        try {
            flags_envia_servidor.put(mac,!flags_envia_servidor[mac]!!)
        }catch (e: KotlinNullPointerException)
        {
            flags_envia_servidor.put(mac,true)
        }
    }
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        when(p0)
        {
            sensibilidade->
            {
                sensibilidade_valor_label.text = p1.toString()
                sensibilidade_distancia=p1
            }
            latencia->
            {
                latencia_valor_label.text = p1.toString()
                tempo_latencia = latencia_valor_label.text.toString().toInt()
            }
        }
    }
    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {
        when(p0)
        {
            latencia->
            {
                if(tempo_latencia == 0)
                    tempo_latencia = 1
                //Timer(true).schedule(Intermitente(this@MainActivity, (tempo_latencia*1000).toLong()), 0 , (tempo_latencia*1000).toLong())
                val query = "UPDATE Configuracao SET beacon_tempo='$tempo_latencia' WHERE idconfig=1"
                acessoBD = AcessoBD(query,this@MainActivity,null,false)
                acessoBD.execute()
            }
        }
    }
    //envia detecção do beacon ao servidor caso o beacon esteja cadastrado
    val notificaDeteccao = { mac : String, deteccao: Long, proxdeteccao: Long ->
        if(beacon_macs.contains(mac))
        {
            //val formater = SimpleDateFormat("hh:mm:ss", Locale.CANADA)
            val requisitaRecurso = RequisitaRecurso("http://${prefs.getString("ip","")}/taxi/www/php/status.php?mac=$mac", this@MainActivity)
            requisitaRecurso.execute()
            flags_envia_servidor.put(mac,false)
        }
    }
    val getDetectados = {
        detectados
    }
    val setDetectados = { lista : ArrayList<String> ->
        detectados.clear()
        detectados.addAll(lista)
    }
    val delDetectado = { obj: String->
        detectados.remove(obj)
    }
    //callback que trata detecção do beacon pelo BleScanner
    private val callback = object : ScanCallback()
    {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            if(callbackType == ScanSettings.CALLBACK_TYPE_FIRST_MATCH || callbackType == ScanSettings.MATCH_MODE_AGGRESSIVE)
            {
                val mac = result!!.device.address
                try {
//                    if(!detectados.contains(mac) && distancia(result.rssi, mac) >= sensibilidade_distancia)
//                        detectados.add(mac)
                    notificaDeteccao(mac, System.currentTimeMillis(), System.currentTimeMillis()+TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()))
                }catch (e: KotlinNullPointerException)
                {
                    e.printStackTrace()
                }catch (e: NullPointerException)
                {
                    e.printStackTrace()
                }catch (e: IndexOutOfBoundsException)
                {
                    e.printStackTrace()
                }
                if(distancia(result.rssi, mac) >= sensibilidade_distancia || sensibilidade_distancia == 0)
                {
                    try {
                        if(rssi_calibrado[mac] == null)
                        {
                            calibragemRssi(result.rssi,mac)
                        }
                        sqlite.readableDatabase.select("Beacon").whereArgs("mac = {deviceMac}", "deviceMac" to mac).exec {
                            while(this.moveToNext())
                            {
                                beacon_numero.text = this.getString(1)
                                mac_detectado.text = mac
                                distancia_valor.text = distancia(result.rssi,mac).toString()
                            }
                            this.close()
                        }
                    }catch(e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    val setMacsUltimaDeteccao = { chave: String, obj: String ->
        mac_ultimaDeteccao.put(chave, obj)
    }
    val setBeaconMacs = { obj: String ->
        beacon_macs.add(obj)
    }
    private val recuperarDadosOnline = { // conecta com o servidor remoto para trazer os novos dados para o sqlite
        beacon_macs.clear()
        sqlite.onUpgrade(sqlite.writableDatabase,sqlite.writableDatabase.version,sqlite.writableDatabase.version+1)
        acessoBD = AcessoBD("SELECT * FROM Beacon",this@MainActivity,null,true)
        acessoBD.execute()
        acessoBD = AcessoBD("SELECT * FROM Carro inner join Situacao on situacaoid=fk_situacao inner join Posto on fk_posto=postoid",this@MainActivity, null, true)
        acessoBD.execute()
    }
    private val distancia = { rssi : Int,mac : String ->
        var rssiAtOneMetter = rssi_calibrado[mac]
        if(rssiAtOneMetter == null)
            rssiAtOneMetter = -38.0
        val distance = Math.pow(10.0,(rssiAtOneMetter - rssi)/20)
        distance
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contask = Conttask()
        sensibilidade.setOnSeekBarChangeListener(this)
        latencia.setOnSeekBarChangeListener(this)
        prefs = defaultSharedPreferences
        editor = prefs.edit()
        if(prefs.getString("ip","").trim() == "" || prefs.getString("ip_banco","").trim() == "" || prefs.getString("banco_nome","").trim() == "" || prefs.getString("banco_login","").trim() == "" || prefs.getString("senha_banco","").trim() == "")
        {
            alert(R.string.config_alert_title) {
                customView {
                    textView {
                        text = getString(R.string.config_alert_text)
                        padding = dip(20)
                    }
                }
                yesButton {
                    val intent = Intent(this@MainActivity, ConfigActivity::class.java)
                    startActivity(intent)
                }
                noButton {  }
            }.show()
        }
        try {
            Timer(true).schedule(contask,0, 1000)
            tempo_latencia = latencia_valor_label.text.toString().toInt()
            Timer(true).schedule(Intermitente(this@MainActivity, (tempo_latencia*1000).toLong()), 0, 1000)
            val query = "UPDATE Configuracao SET beacon_tempo='$tempo_latencia' WHERE idconfig=1"
            acessoDB2 = AcessoBD(query,this@MainActivity,null,false)
            acessoDB2.execute()
        }catch (e: UninitializedPropertyAccessException)
        {
            e.printStackTrace()
        }catch (e: KotlinNullPointerException)
        {
            e.printStackTrace()
        }catch (e: NullPointerException)
        {
            e.printStackTrace()
        }catch (e: IllegalArgumentException)
        {
            e.printStackTrace()
        }
        GetLocalHostTask(ip_gateway, getSystemService(Context.WIFI_SERVICE) as WifiManager).execute()
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        recuperarDadosOnline()
        EstadoDispositivo(this@MainActivity).run()
        when
        {
            bt_adapter == null -> toast(R.string.bt_error)
            bt_adapter?.isEnabled == false -> enableBT()
            else -> {
                scanner = bt_adapter!!.bluetoothLeScanner
                scanner.startScan(callback)
            }

        }
    }
    override fun onResume() {
        recuperarDadosOnline()
        try
        {
            scanner.startScan(callback)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }
        super.onResume()
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        scanner.stopScan(callback)
        when(id)
        {
            R.id.cadastro_beacon->
            {
                val intent = Intent(this@MainActivity, CadastroBeaconActivity::class.java)
                startActivity(intent)
            }
            R.id.relatorio_beacon->
            {
                val intent = Intent(this@MainActivity, RelatorioBeaconActivity::class.java)
                startActivity(intent)
            }
            R.id.scan_beacon->
            {
                val intent = Intent(this@MainActivity, ScanBeaconActivity::class.java)
                startActivity(intent)
            }
            R.id.cadastro_carro->
            {
                val intent = Intent(this@MainActivity, CadastroCarroActivity::class.java)
                startActivity(intent)
            }
            R.id.teste_con->
            {
                val intent = Intent(this@MainActivity, TesteActivity::class.java)
                startActivity(intent)
            }
            R.id.relatorio_carros->
            {
                val intent = Intent(this@MainActivity, RelatorioCarroActivity::class.java)
                startActivity(intent)
            }
            R.id.config->
            {
                val intent = Intent(this@MainActivity, ConfigActivity::class.java)
                startActivity(intent)
            }
            R.id.sair->
            {
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
