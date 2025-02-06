package com.ble.view_sw

import android .content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import com.ble.activity.Scan2Activity
import com.ble.aoaconfig.R
import com.ble.bean.MLog
import kotlin.math.min

/**
 * @package com.ble.view
 * @fileName MyPopwindow
 * @date 2020/6/2916:33
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class MyFilterView: LinearLayout ,View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener, View.OnFocusChangeListener {
    var nameOrMac_Filter:String?="";
    var rawData_Filter:String?="";
    var major=-1;
    var minor=-1;
    var rssi_Filter:Int=-100;
    var status:Boolean=false;
    var focus:Int=0;
    var myFilterViewCallBack:MyFilterViewCallBack?=null;
    var linarlayout: LinearLayout?=null;
    var ed_nameFilter:EditText?=null;
    var ima_nameClose:ImageView?=null;
    var ed_rawFilter:EditText?=null;
    var ima_rawClose:ImageView?=null;
    var rssi_seekBar:SeekBar?=null;
    var ima_rssiClose:ImageView?=null;
    var ed_major:EditText?=null
    var ed_minor:EditText?=null
    var ima_mClose:ImageView?=null;
    var txt_title:TextView?=null
    var ima_scan:ImageView?=null
    //头部
    var linear_filter:LinearLayout?=null;
    //展开
    var linear_FilterDetail:LinearLayout?=null
    constructor(ctx: Context) : super(ctx) {
        init(ctx)
    }


     constructor(context: Context?, attrs: AttributeSet?)  : super(context, attrs){
        Log.e(MLog.NormalE,"初始化了")
      //  LayoutInflater.from(context).inflate(R.layout.title_view, this)
         if (context != null) {
             init(context)
         }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):  super(context, attrs, defStyleAttr) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):  super(context, attrs, defStyleAttr, defStyleRes) {

    }


    private fun init(context: Context) {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        linarlayout = inflater.inflate(R.layout.filter_layout1, this) as LinearLayout?
        linear_filter=linarlayout!!.findViewById(R.id.linear_filter)
        linear_FilterDetail=linarlayout!!.findViewById(R.id.linear_FilterDetail)
        ima_scan=linarlayout!!.findViewById(R.id.ima_scan)
        ed_nameFilter=linarlayout!!.findViewById(R.id.ed_nameFilter)
        ima_nameClose=linarlayout!!.findViewById(R.id.ima_nameClose)
        ed_rawFilter=linarlayout!!.findViewById(R.id.ed_rawFilter)
        ima_rawClose=linarlayout!!.findViewById(R.id.ima_rawClose)
        rssi_seekBar=linarlayout!!.findViewById(R.id.rssi_seekBar)
        ima_rssiClose=linarlayout!!.findViewById(R.id.ima_rssiClose)
        txt_title=linarlayout!!.findViewById(R.id.txt_title)
        ed_major=linarlayout!!.findViewById(R.id.ed_major)
        ed_minor=linarlayout!!.findViewById(R.id.ed_minor)
        ima_mClose=linarlayout!!.findViewById(R.id.ima_mClose)
        ima_rssiClose?.setOnClickListener(this)
        ima_rawClose?.setOnClickListener(this)
        ima_mClose?.setOnClickListener(this)
        ima_nameClose?.setOnClickListener(this)
        rssi_seekBar?.setOnSeekBarChangeListener(this)
        ed_nameFilter?.addTextChangedListener(this)
        ed_rawFilter?.addTextChangedListener(this)
        ed_major?.addTextChangedListener(this)
        ed_minor?.addTextChangedListener(this)
        ed_minor?.onFocusChangeListener=this
        ed_major?.onFocusChangeListener=this
        ed_rawFilter?.onFocusChangeListener = this
        ed_nameFilter?.onFocusChangeListener=this
        linear_filter?.setOnClickListener(View.OnClickListener {
            //TODO("Not yet implemented")
            //myPopwindow!!.showPopupWindow(linear_filter!!);
            onDetailAnimate()
            // bgAlpha(0.5F);

        })
        ima_scan?.setOnClickListener(this)
        Log.e(MLog.NormalE,"高度="+height)
    }


    fun onDetailAnimate() {
      if(status){
          linear_FilterDetail!!.animate()
                  .translationY((-linear_FilterDetail!!.height).toFloat())
                  .setDuration(400)
                  .setInterpolator(AccelerateInterpolator(2F))
                  .start()
      }
        else{
          linear_FilterDetail!!.animate()
                  .translationY((0).toFloat())
                  .setDuration(400)
                  .setInterpolator(AccelerateInterpolator(2F))
                  .start()

      }
        status=!status
    }


    fun onTitlehide(){
        this!!.animate()
                .translationY((-height).toFloat())
                .setDuration(400)
                .setInterpolator(AccelerateInterpolator(2F))
                .start()
    }
    fun onTitleshow(){
        this.animate()
                .translationY((0).toFloat())
                .setDuration(400)
                .setInterpolator(AccelerateInterpolator(2F))
                .start()
    }

    fun hide(){
        linear_FilterDetail?.translationY=(-(linear_FilterDetail?.height)!!).toFloat()
        status=false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View?) {
        //TODO("Not yet implemented")
        if(myFilterViewCallBack==null){
            return;
        }
        when(view?.id){
            ima_rssiClose?.id->
            {
                myFilterViewCallBack?.onRssi(100)
                rssi_seekBar?.setProgress(100,true)
                rssi_Filter=-100;
                onChanged()
            }
            ima_nameClose?.id->
            {
                nameOrMac_Filter=""
                myFilterViewCallBack?.onNameOrMac("")
                ed_nameFilter?.setText("")
                onChanged()
            }

            ima_rawClose?.id->
            {
                myFilterViewCallBack?.onRawData("")
                ed_rawFilter?.setText("")
                onChanged()
            }
            ima_mClose?.id->
            {
                major=-1;
                minor=-1;
                myFilterViewCallBack?.onMajor_Minor(-1,-1)
                ed_minor?.setText("")
                ed_major?.setText("")
                onChanged()
            }
            ima_scan?.id->{
                myFilterViewCallBack?.onScan()
            }
        }

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        //TODO("Not yet implemented")
        when(p0?.id) {
            ed_nameFilter?.id -> {
                focus = 1;
                Log.e(MLog.NormalE, "   focus=" + focus)
            }
            ed_rawFilter?.id -> {
                focus = 2;
                Log.e(MLog.NormalE, "   focus=" + focus)
            }
            ed_major?.id -> {
                focus = 3;
                Log.e(MLog.NormalE, "   focus=" + focus)
            }
            ed_minor?.id -> {
                focus = 4;
                Log.e(MLog.NormalE, "   focus=" + focus)
            }
        }
    }
    override fun afterTextChanged(p0: Editable?) {
        //TODO("Not yet implemented")
       val data= p0.toString().trim()
        if(focus==1){
            myFilterViewCallBack?.onNameOrMac(data)
            nameOrMac_Filter=data;
            onChanged()
        }
        else if(focus==2){
            myFilterViewCallBack?.onRawData(data)
            rawData_Filter=data;
            onChanged()
        }
        else if(focus==3){
          //  myFilterViewCallBack?.onRawData(data)
            if(data.length>0){
                major=data.toInt()
                myFilterViewCallBack?. onMajor_Minor(major, minor)
            }else{
                major=-1;
                myFilterViewCallBack?. onMajor_Minor(major, minor)
            }

        }
        else if(focus==4){
         //   myFilterViewCallBack?.onRawData(data)
            if(data.length>0){
                minor=data.toInt()
                myFilterViewCallBack?. onMajor_Minor(major, minor)
            }else{
                minor=-1;
                myFilterViewCallBack?. onMajor_Minor(major, -1)
            }

        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
       // TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
       // TODO("Not yet implemented")
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        //TODO("Not yet implemented")
     //   p0?.progress?.let { popupWindowCallBack?.onRssi(it) }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        //TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        //TODO("Not yet implemented")
        p0?.progress?.let { myFilterViewCallBack?.onRssi(it)
            rssi_Filter=-it;
            onChanged()
           // dataChanged.onChanged()
        }
    }

     fun onChanged(){
        var data="";
        if(rawData_Filter!!.isNotEmpty()&&nameOrMac_Filter!!.isNotEmpty()){
            data=nameOrMac_Filter+",0x"+rawData_Filter;
            if(rssi_Filter>-100){
                data=nameOrMac_Filter+",0x"+rawData_Filter+","+rssi_Filter+" dBm";
            }
        }else if(rawData_Filter!!.isNotEmpty()){
            data= rawData_Filter as String;
            if(rssi_Filter>-100){
                data="0x"+rawData_Filter+","+rssi_Filter+" dBm";
            }
        }
        else if(nameOrMac_Filter!!.isNotEmpty()){
            data= nameOrMac_Filter as String;
            if(rssi_Filter>-100){
                data=nameOrMac_Filter+","+rssi_Filter+" dBm";
            }
        }
        else   if(rssi_Filter>-100){
            data=rssi_Filter.toString()+" dBm";
        }
        else  if(rssi_Filter==-100){
            data=resources.getString(R.string.filter_tip);
        }
        Log.e(MLog.NormalE,"过滤信号="+rssi_Filter)
        txt_title?.setText(data)
    }
}
