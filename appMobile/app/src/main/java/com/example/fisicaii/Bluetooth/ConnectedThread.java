package com.example.fisicaii.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//Crea la clase que permite crear el evento de conexion
public class ConnectedThread extends Thread {

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler bluetoothIn;
    private final int handlerState = 0;
    private Context context;
    private String readMessage ="";


    public ConnectedThread(BluetoothSocket socket, Context context, Handler bluetoothIn)
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        this.bluetoothIn = bluetoothIn;
        this.context = context;
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[256];
        int bytes;
        // Se mantiene en modo escucha para determinar el ingreso de datos
        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                readMessage = new String(buffer, 0, bytes);
                // Envia los datos obtenidos hacia el evento via handler
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    public String read(){
        return readMessage;
    }
    //Envio de trama
    public void write(String input)
    {
        try {
            mmOutStream.write(input.getBytes());
        }
        catch (IOException e)        {
            //si no es posible enviar datos se cierra la conexión
            Toast.makeText(context.getApplicationContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
            ((Activity)context).finish();
        }
    }
}