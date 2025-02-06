package com.ble.activity
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import butterknife.ButterKnife
import com.ble.MyApplication
import com.ble.beaconconfig.R
import com.ble.beaconconfig.databinding.ActivityShowBinding
import com.ble.bean.Beacon
import com.ble.bean.MLog
import com.ble.bean.Type
import com.ble.presenter.ConnectPresenter
import com.ble.util.StringUtils

import com.ble.view_sw.Pop_Open
import com.ble.view_sw.Pop_password



class BeaconDetailActivity : BaseActivity() ,IBeaconDetailView,View.OnClickListener{
    private lateinit var binding: ActivityShowBinding
    var connectPresenter: ConnectPresenter?=null
    var pop_password : Pop_password?=null
    var pop_open : Pop_Open?=null;
    var connect=0;
    var menu:Menu?=null;
    var type: Type?=null;
    var beacon: Beacon?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_show)
       // ButterKnife.bind(this)

        binding.progressBar.showContextMenu();
    }


    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
   //     TODO("Not yet implemented")
        connectPresenter=ConnectPresenter(this, this)
        MyApplication.connectPresenter=connectPresenter
        val bundle = intent.extras
        val address = bundle?.getString("address")
        pop_password=Pop_password(this, this);
        pop_open= Pop_Open(this,this)
        if (address != null) {
            Log.e(MLog.ConnectE, "界面连接=" + address)
            connectPresenter?.connect(address)
        }else{
            System.out.println("4545")
        }
        beacon=MyApplication.beacon;
        System.out.println("beacon="+beacon?.OpenTemp)

        binding.txtMac.text=address;
        binding.txtPassword.text=""
        binding.txtName.text=beacon?.deviceName
        binding.txtUuid.text=beacon?.uuid;
        binding.txtMajor.text=""+beacon?.major;
        binding.txtMinor.text=""+beacon?.minor;
        binding.txtDbm.text=""+beacon?.txPower+" dBm";
        binding.txtMeasure.text=""+beacon?.rssiInOne+" dBm"
        binding.txtMs.text=""+beacon?.interval+" ms";
        binding.txtEid1.text=beacon?.eid_data;
        binding. txtUidInstance1.text=beacon?.uid_ID_Instance
        binding.txtUidNamespace.text=beacon?.uid_Namespace
        binding.txtTlmEncrypted.text=beacon?.tlm_data
        binding.txtTlmSalt.text=beacon?.tlm_salt
        binding.txtTlmBt1.text= ""+beacon?.tlm_bt+ "mv"
        binding.txtTlmTemp.text=beacon?.tlm_temp;
        binding.txtRssiAt0m.text=""+beacon?.tx_power_at_0m+" dBm"
        binding.txtTlmPdu1.text=""+beacon?.tlm_Advertising_PDU
        binding.txtTlmReboot.text=""+beacon?.tlm_work_time;
        var url=""
    //    binding.txtUrl1.text
        if(beacon?.url_head==0){

            url="http://www."+beacon?.url_data
        }else if(beacon?.url_head==1){
            url="https://www."+beacon?.url_data
        }
        else if(beacon?.url_head==2){
            url="http://"+beacon?.url_data
        }
        else if(beacon?.url_head==3){
            url="https://"+beacon?.url_data
        }
    var end=""
        //  <item>.com/</item>
        //        <item>.org/</item>
        //        <item>.edu/</item>
        //        <item>.net/</item>
        //        <item>.info/</item>
        //        <item>.biz/</item>
        //        <item>.gov/</item>
        //        <item>.com</item>
        //        <item>.org</item>
        //        <item>.edu</item>
        //        <item>.net</item>
        //        <item>.info</item>
        //        <item>.biz</item>
        //        <item>.gov</item>
        when(beacon?.url_tail){
            0->{
                end=".com/"
            }
            1->{
                end=".org/"
            }
            2->{
                end=".edu/"
            }
            3->{
                end=".net/"
            }
            4->{
                end=".info/"
            }
            5->{
                end=".biz/"
        }
            6->{
                end=".gov/"
            }
            7->{
                end=".com"
            }
            8->{
                end=".org"
            }
            9->{
                end=".edu"
            }
            10->{
                end=".net"
            }
            11->{
                end=".info"
            }
            12->{
                end=".biz"
            }
            13->{
                end=".gov"
            }


        }
        url=url+end
        binding.txtUrl1.text=url

        if(beacon?.bt!! <=100){
            binding.txtBt?.text=""+beacon?.bt+"%"
        }else {
            binding.txtBt?.text=""+beacon?.bt+"mv"
        }
        if(beacon?.bt==1998){
            binding.beaconBt.visibility=View.GONE;

        }
        if(beacon?.txPower.equals("N/A")){
            binding. beaconMeasure.visibility=View.GONE
        }
        if(beacon?.interval?.toInt() ==0){
            binding.beaconInterval.visibility=View.GONE
        }
        binding.txtBeaconData.text=beacon?.serviceData?.replace(" ", "");
        var     str=resources.getString(R.string.isClose)
        if(beacon?.OpenTemp==true){
            var str = resources.getString(R.string.tip_update_time)
            binding.txtTemps1?.text=""+beacon?.temp_shidu_Time+" "+str
        }else{

            binding.txtTemps1?.text=str
        }
        if(beacon?.isOpenAcc==true){

            var str = resources.getString(R.string.tip_update_time_min)
            binding.txtAcc?.text=""+beacon?.AccChangeAdv+" "+str
        }else{
            binding.txtAcc?.text=str
        }

        if(beacon?.isSupportTempShidu==false){
            binding.beaconOpenTemp.visibility=View.GONE
        }
        if(beacon?.isSupportAcc==false){
            binding.beaconOpenAcc.visibility=View.GONE
        }

        if(beacon?.type==4){
            binding.txtMode.text="Beacon"
            binding.beaconUuid.visibility=View.VISIBLE
            binding.beaconMajor.visibility=View.VISIBLE
            binding.beaconMinor.visibility=View.VISIBLE
            binding.beaconMeasure.visibility=View.VISIBLE
            binding.eidEncrypted.visibility=View.GONE
            binding. uidInstance.visibility=View.GONE
            binding. uidNamespace.visibility=View.GONE
            binding. tlmReboot.visibility=View.GONE
            binding.tlmBt.visibility=View.GONE
            binding.tlmTemp.visibility=View.GONE
            binding.tlmPdu.visibility=View.GONE
            binding. urlData.visibility=View.GONE
            binding.tlmIntegrityCheck.visibility=View.GONE
            binding.TLMEncryptedData.visibility=View.GONE
            binding. tlmSalt.visibility=View.GONE
            binding. eddyRssiat0m.visibility=View.GONE
        }
        if(beacon?.type==0){
            binding.txtMode.text="Eddystone UID"
            binding.beaconUuid.visibility=View.GONE
            binding. beaconMajor.visibility=View.GONE
            binding. beaconMinor.visibility=View.GONE
            binding.beaconMeasure.visibility=View.GONE
            binding.eidEncrypted.visibility=View.GONE
            binding.uidInstance.visibility=View.VISIBLE
            binding.uidNamespace.visibility=View.VISIBLE
            binding. tlmReboot.visibility=View.GONE
            binding.tlmBt.visibility=View.GONE
            binding.tlmTemp.visibility=View.GONE
            binding.tlmPdu.visibility=View.GONE
            binding.urlData.visibility=View.GONE
            binding. tlmIntegrityCheck.visibility=View.GONE
            binding.TLMEncryptedData.visibility=View.GONE
            binding.tlmSalt.visibility=View.GONE
            binding.eddyRssiat0m.visibility=View.VISIBLE
        }
        if(beacon?.type==1){
            binding.txtMode.text="Eddystone URL"
            binding.beaconUuid.visibility=View.GONE
            binding.beaconMajor.visibility=View.GONE
            binding.beaconMinor.visibility=View.GONE
            binding.beaconMeasure.visibility=View.GONE
            binding.eidEncrypted.visibility=View.GONE
            binding. uidInstance.visibility=View.GONE
            binding. uidNamespace.visibility=View.GONE
            binding. tlmReboot.visibility=View.GONE
            binding. tlmBt.visibility=View.GONE
            binding. tlmTemp.visibility=View.GONE
            binding.tlmPdu.visibility=View.GONE
            binding.urlData.visibility=View.VISIBLE
            binding.tlmIntegrityCheck.visibility=View.GONE
            binding.TLMEncryptedData.visibility=View.GONE
            binding.tlmSalt.visibility=View.GONE
            binding.eddyRssiat0m.visibility=View.VISIBLE
        }
        if(beacon?.type==2){
            binding. txtMode.text="Eddystone TLM"
            binding. beaconUuid.visibility=View.GONE
            binding.beaconMajor.visibility=View.GONE
            binding.beaconMinor.visibility=View.GONE
            binding. beaconMeasure.visibility=View.GONE
            binding. eidEncrypted.visibility=View.GONE
            binding.uidInstance.visibility=View.GONE
            binding.uidNamespace.visibility=View.GONE
            binding.urlData.visibility=View.GONE
            if(beacon?.tlm_type==0){
                binding.tlmReboot.visibility=View.VISIBLE
                binding.tlmBt.visibility=View.VISIBLE
                binding.tlmTemp.visibility=View.VISIBLE
                binding.tlmPdu.visibility=View.VISIBLE

                binding.tlmIntegrityCheck.visibility=View.GONE
                binding.TLMEncryptedData.visibility=View.GONE
                binding.tlmSalt.visibility=View.GONE
            }else{
                binding.tlmReboot.visibility=View.GONE
                binding. tlmBt.visibility=View.GONE
                binding. tlmTemp.visibility=View.GONE
                binding.tlmPdu.visibility=View.GONE

                binding.tlmIntegrityCheck.visibility=View.VISIBLE
                binding.TLMEncryptedData.visibility=View.VISIBLE
                binding. tlmSalt.visibility=View.VISIBLE
            }
            binding.eddyRssiat0m.visibility=View.GONE
        }
        if(beacon?.type==3){
            binding. txtMode.text="Eddystone EID"
            binding.beaconUuid.visibility=View.GONE
            binding.beaconMajor.visibility=View.GONE
            binding.beaconMinor.visibility=View.GONE
            binding.beaconMeasure.visibility=View.GONE
            binding.eidEncrypted.visibility=View.VISIBLE
            binding.uidInstance.visibility=View.GONE
            binding.uidNamespace.visibility=View.GONE
            binding.tlmReboot.visibility=View.GONE
            binding.tlmBt.visibility=View.GONE
            binding.tlmTemp.visibility=View.GONE
            binding.tlmPdu.visibility=View.GONE
            binding.urlData.visibility=View.GONE
            binding.tlmIntegrityCheck.visibility=View.GONE
            binding.TLMEncryptedData.visibility=View.GONE
            binding.tlmSalt.visibility=View.GONE
            binding. eddyRssiat0m.visibility=View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        connectPresenter?.close()
    }
    override fun onResume() {
        super.onResume()
            //仅用于测
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_connect, menu);
        this.menu=menu;
        return true;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when(connect){
            0 -> {
                menu!!.findItem(R.id.reConnect).isVisible = true
                menu!!.findItem(R.id.disConnect).isVisible = false
            }
            1 -> {
                menu!!.findItem(R.id.disConnect).isVisible = true
                menu!!.findItem(R.id.reConnect).isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){
            R.id.reConnect -> {
                Log.e(MLog.NormalE, "重新连接")
                connectPresenter?.reConnect()
                binding.progressBar.visibility = View.VISIBLE
            }
            R.id.disConnect -> {
                Log.e(MLog.NormalE, "断开")
                connectPresenter?.disconnect();
                connect = 0;
            }
        }
        invalidateOptionsMenu();
        return  true;
    }

    override fun initData() {
        //TODO("Not yet implemented")
    }

    override fun show(tip: String) {
     //   TODO("Not yet implemented")
    }

    override fun showPasswordDialog() {
        var str=resources.getString(R.string.enter_password);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        type=Type.psw;
        pop_password?.setType(type!!,"");
        pop_password?.showAtLocation(this.layout, Gravity.CENTER, 0, 100);
        binding.progressBar.visibility=View.GONE
        backgroundAlpha(0.5f)
    }
     fun backgroundAlpha(f: Float) {
        val lp = window.attributes
        lp.alpha = f
        window.attributes = lp
    }
    override fun connectOk(){
        backgroundAlpha(1f)
        binding.  progressBar.visibility=View.GONE
        connect=1;
        menu!!.findItem(R.id.disConnect).isVisible = true
        menu!!.findItem(R.id.reConnect).isVisible = false
    }
    override   fun connectFail(){
        var str=resources.getString(R.string.password_error);
        runOnUiThread { Toast.makeText(this, str, Toast.LENGTH_LONG).show()}

        type=Type.psw;
        pop_password?.setType(type!!,"");
        pop_password?.showAtLocation(this.layout, Gravity.CENTER, 0, 0);

        backgroundAlpha(0.5f)
    }
    override fun connectDis(){
        var str=resources.getString(R.string.disConnect);
        runOnUiThread {   Toast.makeText(this, str, Toast.LENGTH_LONG).show()
            backgroundAlpha(1f)
            binding. progressBar.visibility=View.GONE
            connect=0;
            menu!!.findItem(R.id.disConnect).isVisible = false
            menu!!.findItem(R.id.reConnect).isVisible = true
        }

    }
    override fun onClick(view: View?) {
        try {
            if (view?.id == R.id.btn_save_pop) {
                System.out.println("点击Type=" + type)
                when (type) {
                    Type.psw -> {
                        var password = pop_password?.getText();
                        if (password!!.isEmpty() || password.length < 4) {
                            var str = resources.getString(R.string.tip_password)
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        } else {
                            binding.progressBar.visibility = View.VISIBLE
                            connectPresenter?.sendPassword(password);
                            binding. txtPassword.text=password
                            beacon?.password=password;
                            pop_password?.dismiss()
                        }
                    }
                    Type.change_psw->{

                        var password = pop_password?.getText();
                        if (password!!.isEmpty() || password.length < 4) {
                            var str = resources.getString(R.string.tip_password)
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        } else {
                            binding.progressBar.visibility = View.VISIBLE
                            connectPresenter?.sendChangePassword(password);
                            binding.txtPassword.text=password
                            beacon?.password=password;
                            pop_password?.dismiss()
                        }

                    }

                    Type.deviceName -> {
                        var name = pop_password?.getText()
                        var str = resources.getString(R.string.tip_nam)
                        if (name?.length == 0 || name?.length!! > 16) {
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        } else {
                            binding.txtName.text = name;
                            name = "$" + name;
                            connectPresenter?.sendValue(name.toByteArray(), type!!);
                            pop_password?.dismiss()

                        }
                    }

                    Type.uuid -> {
                        var uuid = pop_password?.getText()
                        if (uuid?.length != 32 || uuid?.length!! % 2 != 0) {
                            var str = resources.getString(R.string.tip_uuid)
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        } else {
                            var uuids = StringUtils.HexToByteArr("24" + uuid);
                            connectPresenter?.sendValue(uuids, type!!);
                            pop_password?.dismiss()
                            binding.txtUuid.text = uuid;
                        }
                    }

                    Type.serviceData -> {
                        var serviceData = pop_password?.getText()?.trim();
                        var str = resources.getString(R.string.tip_customer)
                        if (serviceData?.length == 0 || serviceData?.length!! > 24 || serviceData?.length!! % 2 != 0) {
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                        } else {
                            var serviceDatas = StringUtils.HexToByteArr("24" + serviceData);
                            connectPresenter?.sendValue(serviceDatas, type!!);
                            pop_password?.dismiss()
                         //   binding.txt_ServerData.text = serviceData;
                        }
                    }

                    Type.major -> {
                        var major = pop_password?.getText();
                        var str = resources.getString(R.string.tip_number)
                        if (major?.length == 0 || major?.length!! > 5) {
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                            return;
                        }

                        var iMajor = major?.toInt()
                        if (iMajor != null) {
                            if (iMajor < 0 || iMajor > 65535) {
                                Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        System.out.println("log=" + iMajor)
                        System.out.println("log=" + major)
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
                        binding.  txtMajor.text = major;
                        connectPresenter?.sendValue(data, type!!);
                        pop_password?.dismiss()


                    }

                    Type.minor -> {
                        var minor = pop_password?.getText();
                        var str = resources.getString(R.string.tip_number)
                        if (minor?.length == 0 || minor?.length!! > 5) {
                            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
                            return;
                        }

                        var iMinor = minor?.toInt()
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
                        pop_password?.dismiss()
                        binding. txtMinor.text = minor;

                    }

                    Type.txpower -> {
                        var dBm = pop_password?.getdBm() as Byte;
                        binding. txtDbm.text = pop_password?.getdBms() + " dBm";
                        val data = ByteArray(2)
                        data[0] = 0x24
                        data[1] = dBm
                        connectPresenter?.sendValue(data, type!!);
                        pop_password?.dismiss()

                    }

                    Type.rssiatone -> {
                        var measure = pop_password?.getMeasure();
                        binding. txtMeasure.text = pop_password?.getb_rssi() + " dBm";
                        val data = ByteArray(2)
                        data[0] = 0x24
                        var aa: Byte? = null;
                        if (measure != null) {
                            var adv = "" + beacon?.interval;
                            when (adv) {
                                "100" ->
                                    aa = 0x01;

                                "200" ->

                                    aa = 0x02;

                                "300" ->
                                    aa = 0x03;

                                "400" ->

                                    aa = 0x04;

                                "500" ->

                                    aa = 0x05;

                                "600" ->

                                    aa = 0x06;

                                "700" ->

                                    aa = 0x07;

                                "800" ->
                                    aa = 0x08;

                                "900" ->
                                    aa = 0x09;

                                "1000" ->
                                    aa = 0x0A;

                            }
                            /*if (aa != null) {
                            data[1]=aa
                        };*/
                            beacon?.rssiInOne = measure.toInt();
                            data[1] = measure;
                            connectPresenter?.sendValue(data, type!!);
                            pop_password?.dismiss()
                        }


                    }

                    Type.interval -> {

                        var adv = pop_password?.getAdv();

                        val data = ByteArray(2)
                        data[0] = 0x24

                        if (adv != null) {
                            var measure = beacon?.rssiInOne;

                            /* if (measure != null) {
                            data[2]=measure?.toByte();
                        };*/
                            data[1] = adv;
                            beacon?.interval = pop_password?.getADVS()?.toLong()!!;
                            connectPresenter?.sendValue(data, type!!);
                            pop_password?.dismiss()
                            binding.txtMs.text = pop_password?.getADVS() + "ms";
                        }


                    }

                    Type.wendu_Open -> {
                        beacon?.temp_shidu_Time = pop_open?.check!!
                        beacon?.isSupportTempShidu = true
                        val data = ByteArray(4)

                            var str = resources.getString(R.string.tip_update_time)
                        binding. txtTemps1.setText("" + beacon?.temp_shidu_Time +" "+ str)
                            beacon?.OpenTemp = true
                            data[0] = 0x24
                            data[1] = 0x01
                            val dd = beacon?.temp_shidu_Time?.let {
                                StringUtils.toByteArray(
                                    it,
                                    2
                                )
                            } as ByteArray
                            data[2] = dd[1]
                            data[3] = dd[0]

                        connectPresenter?.sendValue(data, type!!);
                        pop_open?.dismiss()
                    }

                    Type.ACC_Open -> {
                        beacon?.AccChangeAdv = pop_open?.accs!!
                        //beacon?.isSupportAcc= pop_open?.support_acc == true
                        val data = ByteArray(4)

                            beacon?.isOpenAcc(true)
                            var str = resources.getString(R.string.tip_update_time_min)
                        binding. txtAcc.setText("" + beacon?.AccChangeAdv+" " + str)
                            data[0] = 0x24
                            data[1] = 0x01
                            val dd = beacon?.AccChangeAdv?.let {
                                StringUtils.toByteArray(
                                    it,
                                    2
                                )
                            } as ByteArray
                            data[2] = dd[1]
                            data[3] = dd[0]

                        connectPresenter?.sendValue(data, type!!);
                        pop_open?.dismiss()
                    }

                    else -> {}
                }
                return;
            }
            if (connect == 0) {
                connectPresenter?.reConnect()
                binding.progressBar.visibility = View.VISIBLE
                return;
            }
            var intent:Intent?=null
            var stt = resources.getString(R.string.tip_mac_edit)
            when (view?.id) {

                R.id.beacon_mac ->
                    Toast.makeText(this, stt, Toast.LENGTH_SHORT).show()

                R.id.beacon_name -> {

                    beacon?.let { showPop(Type.deviceName, it.deviceName) }
                }

                R.id.beacon_uuid -> {

                    beacon?.let { it.uuid?.let { it1 -> showPop(Type.uuid, it1) } }

                }

                R.id.beacon_sdata -> {

                    beacon?.let { it.serviceData?.let { it1 -> showPop(Type.serviceData, it1) } }

                }

                R.id.beacon_major -> {
                    beacon?.let { it.major?.let { it1 -> showPop(Type.major, "" + it1) } }

                }

                R.id.beacon_minor -> {
                    beacon?.let { it.minor?.let { it1 -> showPop(Type.minor, "" + it1) } }

                }

                R.id.beacon_dbm -> {
                    showPop(Type.txpower, binding.txtDbm.text.toString());
                }

                R.id.beacon_measure -> {
                    showPop(Type.rssiatone, binding.txtMeasure.text.toString());
                }

                R.id.beacon_interval -> {
                    showPop(Type.interval, binding.txtMs.text.toString());
                }

                R.id.beacon_openTemp -> {
                    beacon?.OpenTemp?.let { showPopOpen(Type.wendu_Open, it) }
                }

                R.id.beacon_openAcc -> {
                    beacon?.isOpenAcc?.let { showPopOpen(Type.ACC_Open, it) }
                }
                R.id.beacon_password->{
                    beacon?.password?.let { showPop(Type.change_psw, it) }
                }
                R.id.change_mode->{
                    DialogThree(this)
                }
                R.id.eid_Encrypted->{
                    intent=Intent(this,EidActivity::class.java)
                    startActivity(intent)
                }
                R.id.uid_instance->{
                    intent=Intent(this,UidActivity::class.java)
                    startActivity(intent)
                }
                R.id.uid_namespace->{
                    intent=Intent(this,UidActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_reboot->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_bt->{

                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_temp->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_pdu->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.url_data->{
                    intent=Intent(this,UrlActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_Integrity_Check->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.TLM_Encrypted_data->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.tlm_Salt->{
                    intent=Intent(this,TlmActivity::class.java)
                    startActivity(intent)
                }
                R.id.eddy_rssiat0m->{
                    if(beacon?.type==0){
                        intent=Intent(this,UidActivity::class.java)
                    }else if(beacon?.type==1){
                        intent=Intent(this,UrlActivity::class.java)
                    }
                    else if(beacon?.type==3){
                        intent=Intent(this,EidActivity::class.java)
                    }
                    startActivity(intent)
                }

            }
        }catch ( e:Exception){
            Toast.makeText(this,resources.getString(R.string.data_error),Toast.LENGTH_LONG).show();
        }

    }
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        System.out.println("输出显示log1222222222222")
        return if (pop_password != null && pop_password!!.isShowing) {
            System.out.println("输出显示log")
            false
        } else super.dispatchTouchEvent(event)
    }
    fun  DialogThree(context:Context){
         val array= arrayOf("Eddystone UID","Eddystone URL","Eddystone TLM","Eddystone EID","Beacon")

       var  yourchoice =beacon?.type
        var singlechoiceDialog: AlertDialog.Builder =AlertDialog.Builder(context,0)
        singlechoiceDialog.setTitle(resources.getString(R.string.change_mode))

       // singlechoiceDialog.setTitle("这是一个单选Dialog");//第二个参数是默认选项，此处设置为0
        beacon?.let {
            singlechoiceDialog.setSingleChoiceItems(array,
                it.type,DialogInterface.OnClickListener(){ a, b->
                  //  Toast.makeText(context,""+b,+Toast.LENGTH_LONG).show()
                    yourchoice=b
                })
        }

        singlechoiceDialog.setPositiveButton( "确定", DialogInterface.OnClickListener(){a,b->
          //  Toast.makeText(context,array[yourchoice!!],+Toast.LENGTH_LONG).show()
                if(yourchoice==4){
                    val data = ByteArray(3)
                    data[0]=0x24;
                    data[1]=0x31
                    data[2]=0x30
                    connectPresenter?.sendValue(data,Type.change_mode)
                    connectPresenter?.disconnect()
                    finish();
                }else{
                    var intent:Intent?=null;
                    if(yourchoice==0){
                        intent=Intent(this,UidActivity::class.java)

                    }
                    else if(yourchoice==3){
                        intent=Intent(this,EidActivity::class.java)
                    }

                    else if(yourchoice==1){
                        intent=Intent(this,UrlActivity::class.java)
                    }
                    else if(yourchoice==2){
                        intent=Intent(this,TlmActivity::class.java)
                    }
                    startActivity(intent);
                }
            }
        )
        singlechoiceDialog.show()
    }

    override fun setTemp(data: ByteArray){

        System.out.println(StringUtils.ByteArrToHex(data))
        runOnUiThread{
            var a=  data[0].toInt()
            if(a==1){
                var str=resources.getString(R.string.tip_update_time)
                beacon?.OpenTemp=true
                var time=(data[1].toInt() and 0xFF)* 256 +(data[2].toInt() and 0xFF)
                binding. txtTemps1.text=""+time+" "+str
            }
            else{
                var str=resources.getString(R.string.tip_close_sensor)
                binding. txtTemps1.text=str
            }
        }
    }

    override fun showData(data: ByteArray) {
     /* var d=StringUtils.ByteArrToHex(data);
        Log.e("Log",d)*/
        if(data!=null&&data.size>=3){
            if(data[1]==4.toByte()){
                binding. txtDbm.text=""+data[2]+" dBm";
            }else if(data[1]==5.toByte()){
                var t=0xff.toByte();
                var time=(data[2].toInt() and 0xFF)* 256 +(data[3].toInt() and 0xFF)
                binding.  txtMs.text=""+time+" ms"
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    override fun setAcc(data: ByteArray){
        runOnUiThread{
          var a=  data[0].toInt()
            if (a ==1) {
                beacon?.isOpenAcc=true
                /*when(data[2]){
                    0x01 as Byte->
                    {
                        beacon?.AccChangeAdv=100;
                    }
                    0x02 as Byte->
                    {
                        beacon?.AccChangeAdv=200;
                    }
                    0x03 as Byte->
                    {
                        beacon?.AccChangeAdv=300;
                    }
                    0x04 as Byte->
                    {
                        beacon?.AccChangeAdv=400;
                    }
                    0x05 as Byte->
                    {
                        beacon?.AccChangeAdv=500;
                    }
                    0x06 as Byte->
                    {
                        beacon?.AccChangeAdv=600;
                    }
                    0x07 as Byte->
                    {
                        beacon?.AccChangeAdv=700;
                    }
                    0x08 as Byte->
                    {
                        beacon?.AccChangeAdv=800;
                    }
                    0x09 as Byte->
                    {
                        beacon?.AccChangeAdv=900;
                    }
                    0x10 as Byte->
                    {
                        beacon?.AccChangeAdv=1000;
                    }
                    0x0A as Byte->
                    {
                        beacon?.AccChangeAdv=1500;
                    }
                    0x0B as Byte->
                    {
                        beacon?.AccChangeAdv=2000;
                    }
                    0x0C as Byte->
                    {
                        beacon?.AccChangeAdv=2500;
                    }
                    0x0D as Byte->
                    {
                        beacon?.AccChangeAdv=3000;
                    }
                    0x0E as Byte->
                    {
                        beacon?.AccChangeAdv=5000;
                    }
                    0x0F as Byte->
                    {
                        beacon?.AccChangeAdv=10000;
                    }*/

                var adv=(data[1].toInt() and 0xFF)* 256 +(data[2].toInt() and 0xFF)
                System.out.println("数据="+(data[2].toInt() and 0xFF))
                beacon?.AccChangeAdv=adv;
                var str = resources.getString(R.string.tip_update_time_min)

                binding. txtAcc?.text=""+beacon?.AccChangeAdv+" "+str

            }
            else{
                var str = resources.getString(R.string.tip_close_acc_sensor)
                binding. txtAcc?.text=str

            }
        }

    }

    override fun showVersion(version: String) {
        binding.   txtVersion?.text=version
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (pop_password != null && pop_password!!.isShowing) {
            System.out.println("输出显示log")
            false
        } else return super.onTouchEvent(event)
    }
    fun showPop(type: Type, string: String){
        this.type=type;
        pop_password?.setType(type!!,string)
        pop_password?.showAtLocation(this.layout, Gravity.CENTER, 0, 0);
        binding. progressBar.visibility=View.GONE
       // backgroundAlpha(0.5f)
        pop_password?.setText(string);
    }

    fun showPopOpen(type: Type,b:Boolean){
        this.type=type;
        pop_open?.setType(type!!,b)
        pop_open?.showAtLocation(this.layout, Gravity.CENTER, 0, 0);
        binding.progressBar.visibility=View.GONE
        // backgroundAlpha(0.5f)

    }
}

