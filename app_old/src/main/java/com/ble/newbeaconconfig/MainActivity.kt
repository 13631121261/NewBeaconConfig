package com.ble.newbeaconconfig


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class MainActivity : Activity() {
    var web_view:WebView?=null
    var sp:SharedPreferences?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.main_layout)
        web_view=findViewById(R.id.web_view)
        val webSettings: WebSettings? = web_view?.getSettings();
        webSettings?.setJavaScriptEnabled(true);

        sp=getSharedPreferences("zhihong_tv",0)
        var url=sp?.getString("url","");
        var flag=0;
        if (url != null) {
            if(!url.isEmpty()){
                web_view?.loadUrl(url)
                flag=1;
            }
        }
        if(flag==0){
            showDialog()
        }

    }

    override fun onPause() {
        super.onPause()
        finish();
    }
    fun showDialog(){
        val view: View = layoutInflater.inflate(R.layout.half_dialog_view, null)
        val editText = view.findViewById<View>(R.id.editTextText) as EditText
        val dialog: AlertDialog = AlertDialog.Builder(this)

            .setTitle("输入唯一序号") //设置对话框的标题
            .setView(view)
            .setNegativeButton("取消",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                val id = editText.text.toString()
                Toast.makeText(this@MainActivity, id, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                getUrl(id)
            }).create()
        dialog.show()
    }
    fun getUrl(id:String){
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("http://47.112.223.35:8008/getUrl?id="+id)
            .build()

        client.newCall(request).enqueue(object : Callback {

            @SuppressLint("SuspiciousIndentation")
            override  fun onResponse(call: Call, response: Response) {

              var json=JSONObject(String((response.body?.bytes()!!)))
               if(json!=null){
                    if(json.getInt("status")==1){
                        var url=json.getString("data");
                        runOnUiThread {
                            web_view?.loadUrl(url)
                            var edit=sp?.edit();
                            edit?.putString("url",url)
                            edit?.apply();
                            // 在这里调用WebView的方法
                        }

                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }



        })
    }
}

