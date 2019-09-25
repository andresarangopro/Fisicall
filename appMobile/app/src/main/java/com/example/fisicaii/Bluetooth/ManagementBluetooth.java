package com.example.fisicaii.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.example.fisicaii.BluetoothActivity;

import java.io.IOException;
import java.util.UUID;


public final class  ManagementBluetooth{

    public static String address = null;// "20:18:07:20:04:39";
    public Handler bluetoothIn;
    public int handlerState = 0;
    public String[] arduinoMessage = {};
    private BluetoothAdapter btAdapter = null;
    public BluetoothSocket btSocket = null;
    public StringBuilder DataStringIN = new StringBuilder();
    public ConnectedThread myConexionBT;
    private Context context;
    private Activity activity;
    private static ManagementBluetooth connectionObj;

    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC


    public ManagementBluetooth(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        bluetoothIn = new Handler() {
           public void handleMessage(android.os.Message msg) {handlerContent(msg);}
       };
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //==============================================
    //==Recibe datos del arduino print
    //==============================================


    public String[] handlerContent(android.os.Message msg){
        if (msg.what == handlerState) {
            String readMessage = (String) msg.obj;
            DataStringIN.append(readMessage);

            int endOfLineIndex = DataStringIN.indexOf("#");
            if (endOfLineIndex > 0) {
                String dataInPrint = DataStringIN.substring(0, endOfLineIndex);//dato recibido
                String[] data = dataInPrint.split("&");
                if(data.length > 1){
                    Toast.makeText(context, data[0]+"-" +data[1], Toast.LENGTH_SHORT).show();
                    recogniceActionFromTxt(data[0], data[1]);
                    arduinoMessage = data;
                }

                DataStringIN.delete(0, DataStringIN.length());
            }
        }
        return arduinoMessage;
    }

    public void recogniceActionFromTxt(String action, String data){
        switch(action){

            default:{
                break;
            }
        }
    }


    //================================================================
    // ==crea un conexion de salida segura usando el servicio UUID
    //================================================================
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    //Comprueba que el dispositivo Bluetooth  está disponible y
    //solicita que se active si está desactivado
    public void verificarEstadoBT() {
        if(btAdapter==null) {
            Toast.makeText(context, "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
                //Toast.makeText(context, "El dispositivo esta conectado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "El dispositivo esta tratando de conectarse", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    public void resume(){
        //Consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = activity.getIntent();
//        Consigue la direccion MAC desde DeviceListActivity via EXTRA
        address = intent.getStringExtra("device_address");//<-<- PARTE A MODIFICAR >->->
        Toast.makeText(context, address+"--", Toast.LENGTH_SHORT).show();

        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        initSocket(device);
        establecerConexionSocket();
        myConexionBT = new ConnectedThread(btSocket,context,bluetoothIn);
        myConexionBT.start();
    }

    public void initSocket(BluetoothDevice device){
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(context, "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
    }

    public  void establecerConexionSocket(){
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {}
        }
    }

    // Cuando se sale de la aplicación esta parte permite
    // que no se deje abierto el socket
    public void pause(){
        try {
            btSocket.close();
        } catch (IOException e2) {}
    }



}

