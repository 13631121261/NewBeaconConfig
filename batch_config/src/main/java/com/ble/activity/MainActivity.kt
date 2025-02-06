package com.ble.activity



  import android.Manifest
  import android.Manifest.permission.BLUETOOTH_CONNECT
  import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
  import android.annotation.SuppressLint
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
  import android.net.Uri
  import android.os.Build
  import android.os.Bundle
  import android.os.Environment
  import android.provider.Settings
  import android.util.Log
  import android.view.Menu
  import android.view.MenuItem
  import android.view.View
  import android.widget.EditText
  import android.widget.LinearLayout
  import android.widget.RelativeLayout
  import android.widget.Spinner
  import android.widget.TextView
  import android.widget.Toast
  import androidx.annotation.RequiresApi
  import androidx.recyclerview.widget.LinearLayoutManager
  import androidx.recyclerview.widget.RecyclerView
  import com.ble.batchconfig.R
  import com.ble.bean.Beacon
  import com.ble.bean.Beacon_Adapter
  import com.ble.bean.MLog
  import com.ble.bean.MLog.NormalE
  import com.ble.bean.Type
  import com.ble.presenter.ConnectPresenter
  import com.ble.presenter.MainPresenter
  import com.ble.util.StringUtils
  import com.ble.util.Util
  import com.ble.util.Util.readExcel
  import com.ble.util.Util.writeExcel
  import com.ble.view_sw.HidingScrollListener
  import com.ble.view_sw.MyFilterView
  import pub.devrel.easypermissions.EasyPermissions
  import java.io.File


class MainActivity : BaseActivity() , IMainVIew,IBeaconDetailView {
    var connectPresenter: ConnectPresenter?=null
    var myFilterView: MyFilterView? = null;
    var presenter: MainPresenter = MainPresenter(this);
    var relat_ble: RelativeLayout? = null;
    var relat_gps: RelativeLayout? = null;
    var linear_background: LinearLayout? = null
    var open_ble: TextView? = null;
    var open_gps: TextView? = null;
    var ShowMenu: Int = 1;
    var receiver = Receiver()
    var recycler_view: RecyclerView? = null;
    val PERMS_REQUEST_CODE = 202
    var beaconAdapter: Beacon_Adapter? = null;
    var listData = java.util.ArrayList<String>();
    var update: Boolean = true;
   // var relat_target: RelativeLayout? = null;
    var linear_target: LinearLayout? = null

    var ed_target_uuid: EditText? = null
    var ed_target_major: EditText? = null
    var ed_target_minor: EditText? = null
    var sp_target_txpower: Spinner? = null
    var sp_target_interval: Spinner? = null
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        EasyPermissions.requestPermissions(this, "",1001,  MANAGE_EXTERNAL_STORAGE,   Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION);
        /*if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("申请权限111")
            ActivityCompat.requestPermissions(this, permissions, 300);
        }
        println("申请权限222")
        checkBluetoothAndLocationPermission()*/
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBluetoothAndLocationPermission() {
        //判断是否有访问位置的权限，没有权限，直接申请位置权限
        println("申请权限333")

        //判断是否有访问位置的权限，没有权限，直接申请位置权限
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            println("申请权限444")
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 100
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("申请回调="+requestCode)
        var i=0;
        for (item in permissions) {
            print(item)
            println("="+grantResults[i])

            if ((grantResults[i] == -1&& item.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE))) {
                println("=" +  Environment.isExternalStorageManager())
                //   Environment.isExternalStorageManager()
                if (    !Environment.isExternalStorageManager() ) {
                    //如果没有权限，获取权限{
                    val intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }

                /* val intent: Intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)

                 Toast.makeText(this, "请允许所有文件权限", Toast.LENGTH_LONG).show()
                 startActivityForResult(intent, 66)
                 return;*/
            }
            i++;
        }
    }
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(PROVIDERS_CHANGED_ACTION);
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        update = false;
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    override fun initView() {

        ed_target_uuid = findView(R.id.ed_uuid)
        ed_target_major = findView(R.id.ed_major)
        ed_target_minor = findView(R.id.ed_major)
        sp_target_interval = findView(R.id.sp_interval)
        sp_target_txpower = findView(R.id.sp_txpower)
        relat_ble = findView(R.id.relat_ble);
        relat_gps = findView(R.id.relat_gps);
        open_ble = findView(R.id.openBle);
        open_gps = findView(R.id.openGps);
        recycler_view = findView(R.id.recycler_view)
        myFilterView = findView(R.id.myFilterView);
        myFilterView?.iMainVIew = this;
        linear_background = findView(R.id.linear_background)
        linear_background?.visibility = View.VISIBLE
        var layoutManager = LinearLayoutManager(this);
        recycler_view!!.layoutManager = layoutManager;
        beaconAdapter = Beacon_Adapter(this);
        recycler_view!!.adapter = beaconAdapter
        list = ArrayList<Beacon>();
        beaconAdapter!!.data = list;
     //   relat_target = findView(R.id.relat_target)
        linear_target = findView(R.id.linear_target)
        /*relat_target!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //  TODO("Not yet implemented")
                if (linear_target?.visibility == GONE) {
                    linear_target?.visibility = VISIBLE;
                } else {
                    linear_target?.visibility = GONE
                }
            }
        })*/

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
        recycler_view!!.itemAnimator!!.changeDuration = 300;
        recycler_view!!.itemAnimator!!.moveDuration = 300;
        connectPresenter=ConnectPresenter(this, this)
        recycler_view!!.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {
                //   System.out.println("66667影藏")
                //  myFilterView!!.onTitlehide()

            }

            override fun onShow() {
                //    System.out.println("66667显示")
                //    myFilterView!!.onTitleshow()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // System.out.println("66667状态="+newState)
                ScrollState = newState
            }
        })

    }

    override fun initData() {

        if (Util.GPSisOPen(this)) {
            relat_gps!!.visibility = View.GONE;
        } else {
            relat_gps!!.visibility = View.VISIBLE;
        }
        if (Util.BleIsOPen(this)) {
            relat_ble!!.visibility = View.GONE;
        } else {
            relat_ble!!.visibility = View.VISIBLE;
        }


    }

    var ScrollState = 0;
    var mymenu: Menu? = null;
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        mymenu = menu;
        return true;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (ShowMenu) {
            0 -> {
                menu!!.findItem(R.id.stopScan).isVisible = true
                menu!!.findItem(R.id.startScan).isVisible = false
            }

            1 -> {
                menu!!.findItem(R.id.startScan).isVisible = true
                menu!!.findItem(R.id.stopScan).isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var ble_status = Util.BleIsOPen(this@MainActivity);
        var gps_status = Util.GPSisOPen(this@MainActivity);
        if (!gps_status || !ble_status) {
            showTip();
            return false;
        }
        when (item.itemId) {
            R.id.startScan -> {
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
                Log.e(NormalE, "开始扫描")
                presenter.startScan();
            //    myFilterView?.setclearScanCount()
                list?.clear();
//                finish_total=0;
//                myFilterView?.setScanCount(0);
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
                ShowMenu = 0;
                runOnUiThread{beaconAdapter?.notifyDataSetChanged()}
                update = true
                // myFilterView?.hide()
                // myFilterView!!.onTitleshow()
                Thread {
                    while (update) {
                        if (ScrollState == 0) {
                            System.out.println("66667更新")
                            runOnUiThread(Runnable {
                                kotlin.run {
                                  //myFilterView?.setScanCount()
                                    beaconAdapter?.notifyDataSetChanged();
                                }
                            })
                            Thread.sleep(1000);
                        }

                    }
                }.start()
            }

            R.id.stopScan -> {
                Log.e(NormalE, "停止扫描")
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
                presenter.stopScan();
                update = false
                ShowMenu = 1;

            }
        }
        invalidateOptionsMenu();
        return false;
    }*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var ble_status = Util.BleIsOPen(this@MainActivity);
        var gps_status = Util.GPSisOPen(this@MainActivity);
        if (!gps_status || !ble_status) {
            showTip();
            return false;
        }
        when (item.itemId) {
            R.id.startScan -> {
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
                Log.e(NormalE, "开始扫描")

                //    myFilterView?.setclearScanCount()
                list?.clear();
//                finish_total=0;
//                myFilterView?.setScanCount(0);
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
                ShowMenu = 0;
                runOnUiThread{beaconAdapter?.notifyDataSetChanged()}
                update = true
                forNext()
                // myFilterView?.hide()
                // myFilterView!!.onTitleshow()
                Thread {
                    while (update) {
                        if (ScrollState == 0) {
                            System.out.println("66667更新")
                            runOnUiThread(Runnable {
                                kotlin.run {
                                    //myFilterView?.setScanCount()
                                    beaconAdapter?.notifyDataSetChanged();
                                }
                            })
                            Thread.sleep(1000);
                        }

                    }
                }.start()
            }

            R.id.stopScan -> {
                Log.e(NormalE, "停止扫描")
                System.out.println("数据长度=" + beaconAdapter?.data?.size)
             //   presenter.stopScan();
                update = false
                ShowMenu = 1;

            }
        }
        invalidateOptionsMenu();
        return false;
    }

    fun forNext(){

        if(update){
            var beacon=beacon_list?.get(indexaa);
            if (beacon != null) {
                showBeacon(beacon)
            };
        }

    }



    override fun showBeacon(list: ArrayList<Beacon>) {
        //System.out.println("步骤333")
        /* if(beaconAdapter!!.data!=list){
            beaconAdapter!!.data=list;
          //  System.out.println("步骤444")
        }*/

        //  System.out.println("步骤666")
        // beaconAdapter!!.notifyDataSetChanged()
    }

//hasp_beacon = HashMap<String, Beacon>();
    var list: ArrayList<Beacon>? = null;
    var target_key:String ?=null

    var this_beacon:Beacon?=null
    var has_map:HashMap<String,Beacon>?=null;
    var fail_map:HashMap<String,Beacon>?=null;
    var beacon1:Beacon?=null
    var ss=0;

    @SuppressLint("NotifyDataSetChanged")
    override fun showBeacon(beacon: Beacon) {
        ss++;
      //  if(ss%20==0){
            println("下一个=" + beacon.address+"=="+beacon.major+"=--"+beacon.minor)
       // }
        //
     /*   if(beacon.major==50801){
            println("扫描到信标=" + beacon.minor)
        }*/
if(fail_map==null){
    fail_map= HashMap()
}
        if (beaconAdapter!!.data == null) {
            beaconAdapter!!.data = list
            has_map=HashMap()
        }
        if((targetBeacon?.get(beacon.address) !=null||targetBeacon?.get(""+beacon.major+"_"+beacon.minor) !=null)&&target_key.equals("")){
          // println("扫描到信标=" + beacon.minor)
            if(targetBeacon?.get(""+beacon.major+"_"+beacon.minor)!=null){
                target_key=""+beacon.major+"_"+beacon.minor
            }else {
                target_key = beacon.address;
            }


            beacon1= has_map?.get(beacon.address);
            if(beacon1==null){
                beacon1=beacon;
                has_map?.put(beacon.address,beacon)
            }
            if(targetBeacon!![target_key!!]?.get("is_ok")?.equals("OK") == true){
                beacon1?.status=10;
                target_key=""

            }
          else{
                beacon1?.status=-1;
            }
            if(list?.contains(beacon1) == false){
                list?.add(beacon1!!);
                println("长度=="+list?.size)

            }else{
                return
            }


            runOnUiThread{beaconAdapter?.notifyDataSetChanged()}

            println("beacin1="+beacon1?.status)
           // if(beacon1?.status==-1){
                println("进入连接步骤"+list?.size)
                indexaa++;
                presenter.stopScan()
                this_beacon=beacon1;
                connectPresenter?.connect(beacon.address)
           // }
        }else{
          //  println("不符合"+target_key)
            //target_key=""
        }
    }


    override fun toSelectFile() {

        val intent = Intent(Intent.ACTION_GET_CONTENT) //  意图：文件浏览器
        intent.setType("*/*") //无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet|application/vnd.ms-excel");
        startActivityForResult(intent, 22)   //
    }


    override fun showTip() {
        Toast.makeText(this@MainActivity, resources.getText(R.string.tip), Toast.LENGTH_LONG)
            .show();

    }

    private fun bgAlpha(alpha: Float) {
        recycler_view!!.alpha = alpha
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //    Log.e(MLog.NormalE,"广播"+intent?.action);
            //TODO("Not yet implemented")
            when (intent?.action) {
                PROVIDERS_CHANGED_ACTION -> {
                    val lm =
                        context!!.getSystemService(Service.LOCATION_SERVICE) as LocationManager;
                    val isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    Log.e(MLog.NormalE, "GPS状态=" + isEnabled);
                }

                ACTION_STATE_CHANGED -> {
                    val ble_state = Util.BleIsOPen(context);
                    Log.e(MLog.NormalE, "Ble状态=" + ble_state);
                }

                else -> {}
            }
            initData();
        }

    }

    @Override
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        //    myFilterView?.hide()
        super.onWindowFocusChanged(hasFocus);
    }


    override fun onPause() {
        super.onPause()
        presenter.stopScan();
        update = false
        ShowMenu = 1;
        invalidateOptionsMenu();
    }
    var path:String?=null
    var beacon_list:ArrayList<Beacon>?=null
    var targetBeacon:HashMap<String,HashMap<String,String>>?= HashMap()
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == RESULT_OK) {
             val uri = data!!.data
             if ("file".equals(uri!!.scheme, ignoreCase = true)) { //使用第三方应用打开
                 path = uri.path
                 if (path != null) {
                     if(path!!.endsWith("xlsx")){
                         var data=readExcel(File(path), arrayOf("mac", "old_major", "old_minor", "name", "uuid", "major", "minor", "txPower","rssi_1","interval","password","is_ok"))

                          if(data!=null){
                              beacon_list= ArrayList();
                            finish_total=0;
                            list?.clear()
                            if(targetBeacon==null){
                                targetBeacon= HashMap();

                            }else{
                                targetBeacon!!.clear()
                            }

                            target_key=""
                            var a=0;
                            for (item in data) {
                                println("索引=+"+a)
                                var tt="";
                                var d=0;
                                for ((key, value) in item) {
                                    println("dd="+d+"   key="+key)
                                    if(d>2&&d<9){
                                        tt=tt+"_"+value
                                    }
                                    d++;
                                }
                                println("总结数据="+tt)
                                item["data"]=tt
                                if(item["mac"]!=null&&item["mac"]?.length==17){
                              //  if(item?.get("mac")!!.length==17){
                                    println("地址="+item?.get("mac"))
                                    targetBeacon!![item["mac"]!!] = item

                                    if(item?.get("is_ok")==null||item?.get("is_ok").toString().equals("")){
                                        var b=Beacon();
                                        b.address= item?.get("mac").toString();
                                        beacon_list?.add(b);
                                    }


                                }else if(item["old_major"]!=null&&item["old_minor"]!=null&& !item["old_major"].equals("")&& !item["old_minor"].equals("")){
                                    println("地址="+item["old_major"]!!+item["old_minor"]!!)
                                    targetBeacon!![item["old_major"]!!+"_"+item["old_minor"]!!] = item
                                    if(item?.get("is_ok")==null||item?.get("is_ok").toString().equals("")) {
                                        val b = Beacon();

                                        b.major =
                                            item?.get("old_major")?.let { Integer.getInteger(it) }!!
                                        b.minor =
                                            item["old_minor"]?.let { Integer.getInteger(it) }!!
                                        beacon_list?.add(b);
                                    }
                                }
                                if(item["is_ok"].equals("OK")){
                                    finish_total++;

                                }
                                a++;
                               // print(item)
                            }
                            myFilterView?.setScanCount(finish_total)
                            myFilterView?.setTargetCount(targetBeacon!!.size);
                         //   Toast.makeText(this,"Select:"+ targetBeacon!!.size,Toast.LENGTH_LONG).show()
                              Toast.makeText(this,"Select:"+ beacon_list!!.size,Toast.LENGTH_LONG).show()

                        }else{
                            myFilterView?.clearTargetCount();
                            Toast.makeText(this,resources.getText(R.string.file_tip),Toast.LENGTH_LONG).show()
                        }
                     }
                 }
             }
         }
    }

    override fun show(tip: String) {
       // TODO("Not yet implemented")
    }

    override fun showPasswordDialog() {
     //   TODO("Not yet implemented")
       var password= target_key?.let { targetBeacon?.get(it) }?.get("password")
        println(target_key+"信标密码="+password)
        if(password!=null){
            println("输入密码="+password)
            connectPresenter?.sendPassword(password)
        }else{
            var type="";
            if(target_key?.contains(":") == true){
                type="mac"
                writeExcel(path,type,target_key,"异常=密码")
            }else{
                type="major+minor"
                writeExcel(path,type,target_key,"异常=密码")
            }
            println("异常=密码")
            connectPresenter?.disconnect()
        }
    }

    var index=0;
    var indexaa=0;
    @SuppressLint("NotifyDataSetChanged")
    override fun connectOk() {
      //  TODO("Not yet implemented")
        println("密码连接成功")
        index=2;
        this_beacon?.status=index;
        runOnUiThread{beaconAdapter?.notifyDataSetChanged()}
        update_beacon();

    }

    var finish_total=0;
    fun update_beacon(){

        var a=0;
        var maps= target_key?.let { targetBeacon?.get(it) }
        if (maps != null) {
            for(map in maps){
                if(a<=index){
                    a++;
                    continue
                }
                println("当前执行索引="+a)
                if(map.value.length>0){
                    index++;
                    var type:Type?=null
                    when(map.key){
                        "name"->{
                            type=Type.deviceName
                        }
                        "uuid"->{
                            type=Type.uuid
                        }
                        "major"->{
                            type=Type.major
                        }
                        "minor"->{
                            type=Type.minor
                        }
                        "txPower"->{
                            type=Type.txpower
                        }
                        "rssi_1"->{
                            type=Type.rssiatone
                        }
                        "interval"->{
                            type=Type.interval
                        }
                        "password"->{
                            this_beacon?.status=10;
                         runOnUiThread{
                             beaconAdapter?.notifyDataSetChanged()
                         }

                            println("循环到密码部分,信标完成"+target_key)
                            finish_total++;
                            targetBeacon?.get(target_key)?.put("is_ok","OK")
                            println("重新开始扫描,信标完成"+finish_total+"///"+targetBeacon?.size)
                            var type="";
                            try {
                                if (target_key?.contains(":") == true) {
                                    type = "mac"
                                    writeExcel(path, type, target_key, "OK")
                                } else {
                                    type = "major+minor"
                                    writeExcel(path, type, target_key, "OK")
                                }
                            }catch (e:java.io.IOException){
                                println("保存数据异常="+e)
                            }

                            myFilterView?.setScanCount(finish_total);
                            target_key=""
                            connectPresenter?.disconnect()
                            return;
                        }
                    }
                    if (type != null) {
                        println("开始下发，类型="+type)
                        send(type,map.value)
                        return;
                    }else{
                        println("类型异常")
                    }
                }
            }
        }
    }

    override fun connectFail() {
       // TODO("Not yet implemented")
        println("异常=连接失败,可能密码不对")
       // target_key=""
        this_beacon?.status=-2;
        var type="";
        if(target_key?.contains(":") == true){
            type="mac"
            targetBeacon?.get(target_key)?.put("is_ok","密码错误")
            writeExcel(path,type,target_key,"密码错误")
        }else{
            type="major+minor"
            targetBeacon?.get(target_key)?.put("is_ok","密码错误")
            writeExcel(path,type,target_key,"密码错误")
        }
        connectPresenter?.disconnect()
        runOnUiThread{
            beaconAdapter?.notifyDataSetChanged()
        }

        //presenter.startScan()
    }
    override fun connectDis() {
       // TODO("Not yet implemented")
        if(this_beacon?.status!=10){
            target_key?.let { beacon1?.let { it1 -> fail_map?.put(it, it1) } };
            println("断开,可能密码不对,或者链接超时"+fail_map?.size)
            println("Log="+ (targetBeacon?.get(target_key)))
            this_beacon?.status=-3;
            runOnUiThread{
                beaconAdapter?.notifyDataSetChanged()
            }

        }else if(this_beacon?.status==10){
            println("修改完成后主动断开")
        }else{
            println("未知名断开。")
        }
        index=0;
        if((finish_total+ (fail_map?.size ?:0 ))!=targetBeacon?.size){
            target_key=""
            println("未全部完成，把目标清空")
          // presenter.startScan();
            forNext()
          /*   update = true
            ShowMenu = 0;
            invalidateOptionsMenu();*/
        }
        else{

            presenter.stopScan()
            update = false
            ShowMenu = 1;
            invalidateOptionsMenu();
            println("全部完成咯")
          //  Toast.makeText(this,"全部完成",Toast.LENGTH_LONG).show()
        }
    }

    override fun setAcc(data: ByteArray) {
       // TODO("Not yet implemented")
    }

    override fun setTemp(data: ByteArray) {
        //TODO("Not yet implemented")
    }

    override fun showVersion(version: String) {
      //  TODO("Not yet implemented")
    }

    override fun showData(data: ByteArray) {
       // TODO("Not yet implemented")

    }

    override fun onSetOk(type: String) {
      //  TODO("Not yet implemented")
     println("修改："+type+" 完成")
        update_beacon()
    }



    fun send(type:Type,value: String ){
        when (type) {


            Type.deviceName -> {

                var str = resources.getString(R.string.tip_nam)
                if (value.isEmpty() || value.length > 16) {
                    Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                } else {
                    connectPresenter?.sendValue( ("$" + value).toByteArray(), type!!);
                }
            }

            Type.uuid -> {

                if (value?.length != 32 || value?.length!! % 2 != 0) {
                    var str = resources.getString(R.string.tip_uuid)
                    Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                } else {
                    var uuids = StringUtils.HexToByteArr("24" + value);
                    connectPresenter?.sendValue(uuids, type!!);

                }
            }


            Type.major -> {

                var str = resources.getString(R.string.tip_number)
                if (value?.length == 0 || value?.length!! > 5) {
                    Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                    return;
                }

                var iMajor = value?.toInt()
                if (iMajor != null) {
                    if (iMajor < 0 || iMajor > 65535) {
                        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        return;
                    }
                }


                val ss = iMajor?.let { Integer.toHexString(it) }
                val bname = StringUtils.HexToByteArr(ss)
                val data = ByteArray(3)
                data[0] = 0x24
                if (bname.size == 1) {
                    data[1] = 0
                    data[2] = bname.get(0)
                } else if (bname.size == 2) {
                    data[1] = bname.get(0)
                    data[2] = bname.get(1)
                }
                connectPresenter?.sendValue(data, type!!);

            }

            Type.minor -> {

                var str = resources.getString(R.string.tip_number)
                if (value?.length == 0 || value?.length!! > 5) {
                    Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                    return;
                }

                var iMinor = value?.toInt()
                if (iMinor != null) {
                    if (iMinor < 0 || iMinor > 65535) {
                        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                val ss = iMinor?.let { Integer.toHexString(it) }
                val bname = StringUtils.HexToByteArr(ss)
                val data = ByteArray(3)
                data[0] = 0x24
                if (bname.size == 1) {
                    data[1] = 0
                    data[2] = bname.get(0)
                } else if (bname.size == 2) {
                    data[1] = bname.get(0)
                    data[2] = bname.get(1)
                }
                connectPresenter?.sendValue(data, type!!);

            }

            Type.txpower -> {
                var dbm:Byte?=null
                when (value) {
                    "4" -> {
                        dbm = 0x01
                    }
                    "0" -> {
                        dbm = 0x02
                    }
                    "-4" -> {
                        dbm = 0x03
                    }
                    "-8" -> {
                        dbm = 0x04
                    }
                    "-12" -> {
                        dbm = 0x05
                    }
                    "-16" -> {
                        dbm = 0x06
                    }
                    "-20" -> {
                        dbm = 0x07
                    }
                    "-40" -> {
                        dbm = 0x08
                    }
                }
                val data = ByteArray(2)
                data[0] = 0x24
                if (dbm != null) {
                    data[1] = dbm
                }
                connectPresenter?.sendValue(data, type!!);

            }
            Type.rssiatone -> {
                val data = ByteArray(2)
                data[0] = 0x24
                data[1] = value.toInt().toByte();
                connectPresenter?.sendValue(data, type);
            }
            Type.interval -> {

                var adv :Byte?=null

                val data = ByteArray(2)
                data[0] = 0x24
                when (value) {
                    "100" -> {
                        adv = 0x01
                    }
                    "200" -> {
                        adv = 0x02
                    }
                    "300"-> {
                        adv = 0x03
                    }
                    "400" -> {
                        adv = 0x04
                    }
                    "500" -> {
                        adv = 0x05
                    }
                    "600" -> {
                        adv = 0x06
                    }
                    "700" -> {
                        adv = 0x07
                    }
                    "800" -> {
                        adv = 0x08
                    }
                    "900"-> {
                        adv = 0x09
                    }
                    "1000" -> {
                        adv = 0x0A
                    }
                    "1500" -> {
                        adv = 0x0B
                    }
                    "2000" -> {
                        adv = 0x0C
                    }
                    "2500" -> {
                        adv = 0x0D
                    }
                    "3000" -> {
                        adv = 0x0E
                    }
                    "5000" -> {
                        adv = 0x0F
                    }
                    "10000" -> {
                        adv = 0x10
                    }
                }
                if (adv != null) {
                    data[1] = adv
                };
                    connectPresenter?.sendValue(data, type);

                }
            else ->{
                println("遇到意外情况="+type)
            }



        }
        }


}