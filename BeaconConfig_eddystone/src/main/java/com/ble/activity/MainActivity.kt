package com.ble.activity

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationManager.PROVIDERS_CHANGED_ACTION
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ble.MyApplication
import com.ble.beaconconfig.R
import com.ble.bean.Beacon
import com.ble.bean.Beacon_Adapter
import com.ble.bean.MLog
import com.ble.bean.MLog.NormalE
import com.ble.presenter.MainPresenter
import com.ble.util.Util
import com.ble.view_sw.HidingScrollListener
import com.ble.view_sw.MyFilterView


class MainActivity : BaseActivity() , IMainVIew, Beacon_Adapter.NoDeviceCallback ,OnClickListener{
    var myFilterView:MyFilterView?=null;
    var presenter:MainPresenter=MainPresenter(this);
    var relat_ble:RelativeLayout?=null;
    var relat_gps:RelativeLayout?=null;
    var linear_background:LinearLayout?=null
    var open_ble:TextView?=null;
    var open_gps:TextView?=null;
    var ShowMenu:Int=1;
    var  receiver = Receiver()
    var recycler_view: RecyclerView?=null;
     val PERMS_REQUEST_CODE = 202
    var beaconAdapter:Beacon_Adapter?=null;
    var listData=java.util.ArrayList<String>();
    var update:Boolean=true;

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        // if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT)!=PackageManager.PERMISSION_GRANTED){
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT
            )
            ActivityCompat.requestPermissions(this,permissions,300);
        }
        checkBluetoothAndLocationPermission()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkBluetoothAndLocationPermission() {
        //判断是否有访问位置的权限，没有权限，直接申请位置权限
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                            BLUETOOTH_CONNECT
                ), 100
            )
        }
    }

    override fun onScan1() {
        //TODO("Not yet implemented")
        val intent = Intent(this@MainActivity, Scan2Activity::class.java)
    //    mainPresenter.stopScan()
        startActivityForResult(intent, 1234)
    }
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(PROVIDERS_CHANGED_ACTION);
        registerReceiver(receiver, filter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1234&&resultCode==RESULT_OK){
           var address = data?.getStringExtra("mac")
            myFilterView?.ed_nameFilter?.setText(address)
            myFilterView?.nameOrMac_Filter=address
            myFilterView?.onChanged();
            if (address != null) {
                beaconAdapter?.onNameOrMac(address)
            }
            System.out.println("111地址="+address)
          //  presenter.startScan()

/*

                System.out.println("数据长度="+beaconAdapter?.data?.size)
                Log.e(NormalE,"开始扫描")
                presenter.startScan();
                System.out.println("数据长度="+beaconAdapter?.data?.size)
                ShowMenu=0;
                beaconAdapter?.notifyDataSetChanged()
                update=true
                myFilterView?.hide()
                myFilterView!!.onTitleshow()
                Thread {
                    while (update) {
                        if(ScrollState==0){
                            // System.out.println("66667更新")
                            runOnUiThread(Runnable { kotlin.run {    beaconAdapter?.notifyDataSetChanged();} })
                            Thread.sleep(1000);
                        }

                    }
                }.start()
*/

        }
    }
    override fun onDestroy() {
        update=false;
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        relat_ble=findView(R.id.relat_ble);
        relat_gps=findView(R.id.relat_gps);
        open_ble=findView(R.id.openBle);
        open_gps=findView(R.id.openGps);
        recycler_view=findView(R.id.recycler_view)
        myFilterView=findView(R.id.myFilterView);
        linear_background=findView(R.id.linear_background)
        linear_background?.visibility=View.VISIBLE
        var layoutManager =  LinearLayoutManager(this);
        recycler_view!!.layoutManager = layoutManager;
        beaconAdapter=Beacon_Adapter(this);
        recycler_view!!.adapter = beaconAdapter

        myFilterView!!.myFilterViewCallBack=beaconAdapter;
        open_ble!!.setOnClickListener(object : View.OnClickListener {
        override fun onClick(p0: View?) {
            //TODO("Not yet implemented")
            Util.openBle(this@MainActivity);
        }

    });
        open_gps!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
             //   TODO("Not yet implemented")
                Util.openGPS(this@MainActivity);
            }
        });
        recycler_view!!.itemAnimator!!.changeDuration=300;
        recycler_view!!.itemAnimator!!.moveDuration=300;

        recycler_view!!.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                //TODO("Not yet implemented")
                if(myFilterView!!.status){
                    myFilterView!!.hide()
                    return true
                }
                return false
            }

        })
        recycler_view!!.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
             //   System.out.println("66667影藏")
                myFilterView!!.onTitlehide()

            }

            override fun onShow() {
            //    System.out.println("66667显示")
                myFilterView!!.onTitleshow()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
               // System.out.println("66667状态="+newState)
                ScrollState=newState
            }
        })

     //   mToolbar!!.menu.set
        beaconAdapter!!.noDeviceCallback=this
        
    }

    override fun initData() {

        if(Util.GPSisOPen(this)){
            relat_gps!!.visibility=View.GONE;
        }else{
            relat_gps!!.visibility=View.VISIBLE;
        }
        if(Util.BleIsOPen(this)){
            relat_ble!!.visibility=View.GONE;
        }else{
            relat_ble!!.visibility=View.VISIBLE;
        }


    }
    var ScrollState=0;
    var mymenu: Menu?=null;
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        mymenu=menu;
        return true;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when(ShowMenu){
            0->{
                menu!!.findItem(R.id.stopScan).isVisible = true
                menu!!.findItem(R.id.startScan).isVisible = false
            }
            1->{
                menu!!.findItem(R.id.startScan).isVisible = true
                menu!!.findItem(R.id.stopScan).isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var ble_status=Util.BleIsOPen(this@MainActivity);
        var gps_status=Util.GPSisOPen(this@MainActivity);
        if(!gps_status||!ble_status){
            showTip();
           return false;
        }
        when(item.itemId){
            R.id.startScan-> {
                System.out.println("数据长度="+beaconAdapter?.data?.size)
                Log.e(NormalE,"开始扫描")
                presenter.startScan();
                System.out.println("数据长度="+beaconAdapter?.data?.size)
                ShowMenu=0;
                beaconAdapter?.notifyDataSetChanged()
                update=true
                myFilterView?.hide()
                myFilterView!!.onTitleshow()
                Thread {
                    while (update) {
                        if(ScrollState==0){
                           // System.out.println("66667更新")
                            runOnUiThread(Runnable { kotlin.run {    beaconAdapter?.notifyDataSetChanged();} })
                            Thread.sleep(1000);
                        }

                    }
                }.start()
            }
            R.id.stopScan-> {
                Log.e(NormalE, "停止扫描")
                System.out.println("数据长度="+beaconAdapter?.data?.size)
                presenter.stopScan();
                update=false
                ShowMenu=1;
            }
        }
        invalidateOptionsMenu();
        return false;
    }


    override fun showBeacon(list: ArrayList<Beacon>) {
        //System.out.println("步骤333")
        if(beaconAdapter!!.data!=list){
            beaconAdapter!!.data=list;
          //  System.out.println("步骤444")
        }
      //  System.out.println("步骤666")
       // beaconAdapter!!.notifyDataSetChanged()
    }
    override  fun showTip(){
        Toast.makeText(this@MainActivity,resources.getText(R.string.tip),Toast.LENGTH_LONG).show();

    }

    private fun bgAlpha(alpha: Float) {
        recycler_view!!.alpha=alpha
    }
    inner  class Receiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
        //    Log.e(MLog.NormalE,"广播"+intent?.action);
            //TODO("Not yet implemented")
           when(intent?.action){
               PROVIDERS_CHANGED_ACTION->{
                 val lm= context!!.getSystemService(Service.LOCATION_SERVICE)as LocationManager;
                   val isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                   Log.e(MLog.NormalE,"GPS状态="+isEnabled);
               }
               ACTION_STATE_CHANGED->{
                  val ble_state= Util.BleIsOPen(context);
                   Log.e(MLog.NormalE,"Ble状态="+ble_state);
               }
           }
             initData();
        }

    }
    @Override
    override fun onWindowFocusChanged(hasFocus:Boolean ) {
        myFilterView?.hide()
        super.onWindowFocusChanged(hasFocus);
    }

    override fun noData() {
        //TODO("Not yet implemented")
        linear_background?.visibility=View.VISIBLE
    }

    override fun hasdata() {
        //TODO("Not yet implemented")
        linear_background?.visibility=View.GONE
    }

    override fun cclick():Boolean {
      System.out.println("点击一下")
        if(myFilterView?.status == true){
            myFilterView?.hide()
            return true;
        }
/*
        presenter.stopScan();
        update=false
        ShowMenu=1;
        invalidateOptionsMenu();*/

        return false
    }

    override fun onPause() {
        super.onPause()

        presenter.stopScan();
        update=false
        ShowMenu=1;
        invalidateOptionsMenu();
    }
    override fun  onClickConnect(beacon:Beacon){
            presenter.stopScan()
            ShowMenu=1;
            invalidateOptionsMenu();
            var intent=Intent()
            intent.setClass(this,BeaconDetailActivity::class.java)
            intent.putExtra("address",beacon.address)
            MyApplication.beacon=beacon;
            startActivity(intent)
    }

    override fun onClick(p0: View?) {
         //  Toast.makeText(this,"点击",Toast.LENGTH_LONG).show();
        myFilterView?.hide()
    }
}
