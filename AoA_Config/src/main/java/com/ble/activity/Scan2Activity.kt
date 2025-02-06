package com.ble.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ble.aoaconfig.R
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan

class Scan2Activity  : AppCompatActivity() {
    private var framelayout: FrameLayout? = null
    private var remoteView: RemoteView? = null
    private val remoteViewManager: RemoteViewManager by lazy {
        RemoteViewManager(remoteView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan2)
        framelayout=findViewById(R.id.rim1)
        initScanView(savedInstanceState)
    }

    private fun initScanView(savedInstanceState: Bundle?) {
        val dm = resources.displayMetrics
        //2.get screen size
        val density = dm.density
        val mScreenWidth = dm.widthPixels
        val mScreenHeight = dm.heightPixels
        val scanFrameSize = (SCAN_FRAME_SIZE * density)

        val rect = Rect()
        apply {
            rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
            rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
            rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
            rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()
        }

        remoteView = RemoteView.Builder()
            .setContext(this)
            .setBoundingBox(rect)
            //.setFormat(HmsScan.ALL_SCAN_TYPE)
            .setFormat(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE)
            .build()
        remoteView?.setOnResultCallback { result ->
            if (result != null && result.isNotEmpty() && result[0] != null && !TextUtils.isEmpty(
                    result[0].getOriginalValue()
                )
            ) {
                onScanResultCallback(result[0].originalValue)
            }
        }
        remoteViewManager.onCreate(this, savedInstanceState)

        // Add the defined RemoteView to the page layout.
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        framelayout?.addView(remoteView, params)
    }

    private fun onScanResultCallback(text: String?) {
        println("mac="+text?.length)
        if(text==null){
            Toast.makeText(this, "扫码内容为空", Toast.LENGTH_SHORT).show()
        }else if(text.length!=17&&text.length!=12){
            Toast.makeText(this, "信标地址格式不正确", Toast.LENGTH_SHORT).show()
        }else{
           var text1=text.replace(":","")
            val intent = Intent()
            intent.putExtra("mac", text1)
            System.out.println("1113地址="+text1)
            setResult(RESULT_OK, intent)
            finish()
        }


    }

    companion object {
        const val SCAN_FRAME_SIZE = 300
    }
}