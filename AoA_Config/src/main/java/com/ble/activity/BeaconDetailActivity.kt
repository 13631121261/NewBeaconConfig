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
import com.ble.aoaconfig.R
import com.ble.aoaconfig.databinding.ActivityShowBinding
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

        binding.txtMac.text=address;

        binding.txtDbm.text=""+beacon?.txPower+" dBm";

        binding.txtMs.text=""+beacon?.interval+" ms";



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


            binding.txtBt?.text=""+beacon?.bt



        if(beacon?.interval?.toInt() ==0){
            binding.beaconInterval.visibility=View.GONE
        }

        var     str=resources.getString(R.string.isClose)




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

                            name = "$" + name;
                            connectPresenter?.sendValue(name.toByteArray(), type!!);
                            pop_password?.dismiss()

                        }
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




                R.id.beacon_dbm -> {
                    showPop(Type.txpower, binding.txtDbm.text.toString());
                }


                R.id.beacon_interval -> {
                    showPop(Type.interval, binding.txtMs.text.toString());
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


        var singlechoiceDialog: AlertDialog.Builder =AlertDialog.Builder(context,0)
        singlechoiceDialog.setTitle(resources.getString(R.string.change_mode))

       // singlechoiceDialog.setTitle("这是一个单选Dialog");//第二个参数是默认选项，此处设置为0






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

