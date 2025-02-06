package com.ble.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ble.bean.MLog.*
import com.tbruyelle.rxpermissions3.RxPermissions


abstract  class BaseActivity : AppCompatActivity() {
    var layout: ViewGroup?=null;
    var layout_id:Int=0;
    val rxPermissions = RxPermissions(this)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        super.onStart()
        /*rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .subscribe { granted ->
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                        Log.e(NormalE,"允许")
                    } else {
                        // Oups permission denied

                    }
                }*/
      /*  val permissions = arrayOf<String>(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

        )
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 200)
        }*/
        checkBluetoothAndLocationPermission()
    }
    @TargetApi(Build.VERSION_CODES.M)
    private  fun checkBluetoothAndLocationPermission() {

/*

        //判断是否有访问位置的权限，没有权限，直接申请位置权限
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 100
            )
        }*/
    }
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        layout= window.decorView as ViewGroup;
        initView();
        initData();
    }
    override fun setContentView(view: View?) {
        super.setContentView(view)
        layout= window.decorView as ViewGroup;
        initView();
        initData();
    }
    protected fun <T : View?> findView(id: Int): T {
        return findViewById<View>(id) as T
    }

    abstract fun initView();
    abstract fun initData();

}
