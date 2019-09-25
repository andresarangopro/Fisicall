package com.example.fisicaii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.fisicaii.Bluetooth.ManagementBluetooth
import kotlinx.android.synthetic.main.activity_app.*

class AppActivity : AppCompatActivity(), View.OnClickListener{
    var managementBluetooth:ManagementBluetooth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        managementBluetooth = ManagementBluetooth(this.applicationContext, this)
        managementBluetooth!!.verificarEstadoBT()
        button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        v as Button
        when(v.id){
            R.id.button->{managementBluetooth!!.myConexionBT.write("1")}
        }
    }

    override fun onResume() {
        super.onResume()
        managementBluetooth!!.resume()
    }


    override fun onPause() {
        super.onPause()
        managementBluetooth!!.pause()
    }

}
