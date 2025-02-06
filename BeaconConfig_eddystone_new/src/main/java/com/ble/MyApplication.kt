package com.ble

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ble.beaconconfig.R

import com.ble.bean.Beacon

import com.ble.presenter.ConnectPresenter

import kotlin.system.exitProcess


/**
 * @package com.ble
 * @fileName MyApplication
 * @date 2020/6/2217:25
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class MyApplication : Application() {
   // var connectPresenter: ConnectPresenter?=null

    companion object {
        var  beacon: Beacon?=null
       var connectPresenter: ConnectPresenter?=null
    }

    var mBluetoothAdapter: BluetoothAdapter? = null
    override fun onCreate() {
        super.onCreate()
        init();
    }
   fun init() {
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
       if (mBluetoothAdapter == null) {
            Toast.makeText(this,resources.getText(R.string.noSupportBle),Toast.LENGTH_LONG).show();
            exitProcess(0);//正常退出
       }
       if(!mBluetoothAdapter!!.isEnabled){
           if (ActivityCompat.checkSelfPermission(
                   this,
                   Manifest.permission.BLUETOOTH_CONNECT
               ) != PackageManager.PERMISSION_GRANTED
           ) {
               // TODO: Consider calling
               //    ActivityCompat#requestPermissions
               // here to request the missing permissions, and then overriding
               //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
               //                                          int[] grantResults)
               // to handle the case where the user grants the permission. See the documentation
               // for ActivityCompat#requestPermissions for more details.
               return
           }
           mBluetoothAdapter!!.enable();
       }

    }



}