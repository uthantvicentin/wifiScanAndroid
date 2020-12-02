package com.example.wifiscan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

   var basededados = ""

   private var wifiManager: WifiManager? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonscan = findViewById<Button>(R.id.btnScan)
        val buttonsave = findViewById<Button>(R.id.btnSave)

        wifiManager =  applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val success = wifiManager!!.startScan()
        buttonscan?.setOnClickListener() {
            scanSuccess()
        }
        buttonsave?.setOnClickListener(){
            Toast.makeText(this@MainActivity, "Salvando amostras", Toast.LENGTH_LONG).show()
            save()
            Toast.makeText(this@MainActivity, "Amostras salvas", Toast.LENGTH_LONG).show()
        }
    }
    private fun scanSuccess() {

        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    val mScanResults: List<ScanResult> = wifiManager!!.getScanResults()
                    val texto = findViewById<EditText>(R.id.tLocal)
                    //val results = wifiManager!!.scanResults
                    for (result in mScanResults) basededados = result.BSSID + "\n" + result.level + "\n"
                    basededados += "Round\n"
                    basededados = basededados + texto.getText() + "\n"
                    println(basededados)
                    Toast.makeText(this@MainActivity, "Amostra finalizada", Toast.LENGTH_LONG).show()

                }
            }
        }

        Toast.makeText(this@MainActivity, "Iniciando Amostra", Toast.LENGTH_LONG).show()
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
/*
        val texto = findViewById<EditText>(R.id.tLocal)
        val qt = findViewById<EditText>(R.id.tqtScanNum).text.toString().toInt()
        var i = 1

        while (i <= qt) {
            //val success = wifiManager!!.startScan()
            if (success) {
                basededados = ""
                val results = wifiManager!!.scanResults
                for (result in results) basededados = result.BSSID + "\n" + result.level + "\n"
                basededados += "Round\n"
                basededados = basededados + texto.getText() + "\n"
                println(basededados)
                Toast.makeText(this@MainActivity, "Amostra " + i + " finalizada", Toast.LENGTH_LONG).show()
            }

            else {
                println("DEU RUIM")
            }
            ++i
        }


 */
    }

    private fun save(){
        try {
            val outputStreamWriter = OutputStreamWriter(applicationContext.openFileOutput("config.txt", Context.MODE_PRIVATE))
            outputStreamWriter.write(basededados)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}