package com.example.fisicaii

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class BluetoothActivity : AppCompatActivity() {
    // Depuración de LOGCAT
    private val TAG = "DispositivosBT" //<-<- PARTE A MODIFICAR >->->
    // Declaracion de ListView
    var IdLista: ListView?=null
    // String que se enviara a la actividad principal, mainactivity
    public var EXTRA_DEVICE_ADDRESS = "device_address"

    // Declaracion de campos
    private var mBtAdapter: BluetoothAdapter? = null
    private var mPairedDevicesArrayAdapter: ArrayAdapter<Any>? = null
    private val pairedDevicesArray: Array<BluetoothDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        //---------------------------------
        VerificarEstadoBT()

        // Inicializa la array que contendra la lista de los dispositivos bluetooth vinculados
        mPairedDevicesArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)//<-<- PARTE A MODIFICAR >->->
        // Presenta los disposisitivos vinculados en el ListView
        IdLista = findViewById(R.id.IdLista) as ListView
        IdLista!!.setAdapter(mPairedDevicesArrayAdapter)
        IdLista!!.setOnItemClickListener(mDeviceClickListener)
        // Obtiene el adaptador local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        //------------------- EN CASO DE ERROR -------------------------------------
        //SI OBTIENES UN ERROR EN LA LINEA (BluetoothDevice device : pairedDevices)
        //CAMBIA LA SIGUIENTE LINEA POR
        //Set <BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //------------------------------------------------------------------------------

        // Obtiene un conjunto de dispositivos actualmente emparejados y agregua a 'pairedDevices'
        val pairedDevices = mBtAdapter!!.getBondedDevices()

        /*if(null != pairedDevices){
            pairedDevicesArray = (BluetoothDevice[]) pairedDevices.toArray();
            for (int i = 0; i< pairedDevicesArray.length; i++){
                mPairedDevicesArrayAdapter.add( pairedDevicesArray[i].getName() + "\n" + pairedDevicesArray[i].getName() );
            }
        }*/
        // Adiciona un dispositivos previo emparejado al array
        if (pairedDevices.size > 0) {
            for (device:BluetoothDevice in pairedDevices) { //EN CASO DE ERROR LEER LA ANTERIOR EXPLICACION
                mPairedDevicesArrayAdapter!!.add( device.name+ "\n" + device.address)
            }
        }
    }
    private val mDeviceClickListener =
        AdapterView.OnItemClickListener { av, v, arg2, arg3 ->
            // Obtener la dirección MAC del dispositivo, que son los últimos 17 caracteres en la vista
            val info = (v as TextView).text.toString()
            val addres = info.substring(info.length - 17)
            //     address = addres;
            // Realiza un intent para iniciar la siguiente actividad
            // mientras toma un EXTRA_DEVICE_ADDRESS que es la dirección MAC.
            val i = Intent(this,AppActivity::class.java)//<-<- PARTE A MODIFICAR >->->
            i.putExtra(EXTRA_DEVICE_ADDRESS, addres)
            startActivity(i)
        }
    
    @SuppressLint("MissingPermission")
    private fun VerificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido.
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBtAdapter == null) {
            Toast.makeText(baseContext, "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (mBtAdapter!!.isEnabled) {
                Log.d(TAG, "...Bluetooth Activado...")
            } else {
                //Solicita al usuario que active Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)

            }
        }
    }


}
