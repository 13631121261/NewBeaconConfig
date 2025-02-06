package com.ble.view_sw

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.ble.activity.IMainVIew
import com.ble.batchconfig.R
import com.ble.bean.MLog

/**
 * @package com.ble.view
 * @fileName MyPopwindow
 * @date 2020/6/2916:33
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
class MyFilterView: LinearLayout ,View.OnClickListener {

    var iMainVIew:IMainVIew?=null;
    var linarlayout: LinearLayout?=null;
    var  btn_select_file:Button?=null;
    var txt_target_count:TextView?=null
    var txt_title:TextView?=null
    //头部
    var linear_filter:LinearLayout?=null;
    //展开

    var txt_scan_count:TextView?=null
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
        txt_title=linarlayout!!.findViewById(R.id.txt_title)
        txt_scan_count=linarlayout!!.findViewById(R.id.txt_scan_count)
        btn_select_file=linarlayout!!.findViewById(R.id.btn_select_file)
        txt_target_count=linarlayout!!.findViewById(R.id.txt_target_count)
        btn_select_file?.setOnClickListener(this)
        Log.e(MLog.NormalE,"高度="+btn_select_file)

    }


    @SuppressLint("SetTextI18n")
    fun setScanCount(count:Int){
        println("执行666")
        txt_scan_count?.text= ""+context.getText(R.string.config_count) +(count)
    }
    fun setclearScanCount(){
        println("执行555")
        txt_scan_count?.text= ""+context.getText(R.string.config_count) + "0"
    }

    @SuppressLint("SetTextI18n")
    fun setTargetCount(count:Int){
        txt_target_count?.text= ""+context.getText(R.string.target_count) +(count)
    }
    fun clearTargetCount(){
        txt_target_count?.text= ""+context.getText(R.string.target_count) + "0"
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(view: View?) {
        iMainVIew?.toSelectFile();
    }
}
