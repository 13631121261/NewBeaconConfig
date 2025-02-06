package com.ble.newbeaconconfig

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.webkit.WebSettings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private var mVideoView: VideoView? = null
    private var mImageView: ImageView? = null




 //   private val host="http://47.112.223.35:8008"
 private val host="http://192.168.1.14:8008"




    // 假设我们有若干随机链接，这里仅示例写死
    private var randomLinks: MutableList<String> = ArrayList()

    // 记录已下载到本地的媒体文件路径
    private val mediaPathList: MutableList<String> = ArrayList()

    // 当前播放的媒体索引
    private var currentIndex = 0

    // 用于控制图片的切换时机（10秒后切换），或者视频播放完毕后切换
    private val mHandler = Handler(Looper.getMainLooper())

    // 用于延迟切换到下一个媒体的 Runnable
    private val nextMediaRunnable = Runnable { playNextMedia() }

     var url:String="";

    var sp: SharedPreferences?=null;
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mVideoView = findViewById<VideoView>(R.id.video_view)
        mImageView = findViewById<ImageView>(R.id.image_view)
        sp=getSharedPreferences("zhihong_tv",0)
         url= sp?.getString("url","").toString();
        var serial=sp?.getString("serial","")
        if (!serial.equals("")) {
                checkAndRequestPermissions()
        }
        else {
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
                 url=host+"/Api/getAll?id="+id;
                sp?.edit()?.putString("url",url)?.apply();
                getUrl(url)
            }).create()
        dialog.show()
    }
    fun getUrl(url:String){
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
      /*  val request: Request = Request.Builder()
            .url(host+"/Api/getAll?id="+id)
            .build()*/
        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("SuspiciousIndentation")
            override  fun onResponse(call: Call, response: Response) {

                var json= JSONObject(String((response.body?.bytes()!!)))
                if(json!=null){
                    if(json.getInt("code")==1){
                        var data=json.getJSONArray("data");
                        runOnUiThread {
                            mediaPathList.clear();
                            val serial=UUID.randomUUID().toString()
                            sp?.edit()?.putString("serial",serial)?.apply()
                            for (inex in 0 ..data.length()-1){
                                println("具体链接="+host+"/Api/download?path="+data.getJSONObject(inex).getString("url")+"&serial="+serial+"&type="+data.getJSONObject(inex).getInt("type"))
                                randomLinks.add(host+"/Api/download?path="+data.getJSONObject(inex).getString("url")+"&serial="+serial+"&type="+data.getJSONObject(inex).getInt("type"))
                                downloadFile((host+"/Api/download?path="+data.getJSONObject(inex).getString("url")+"&serial="+serial+"&type="+data.getJSONObject(inex).getInt("type")))
                            }
                        }

                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }



        })
    }













    /**
     * 检查并申请权限
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkAndRequestPermissions() {
     /*   if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            println("没有权限，请求权限")*/
           /* ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )*/
            if (    !Environment.isExternalStorageManager() ) {
                //如果没有权限，获取权限{
                val intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                println(" 已有权限，初始化媒体列表")
                // 已有权限，初始化媒体列表
                initMediaListAndPlay()
            }
       // }
    }

    /**
     * 权限请求回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMediaListAndPlay()
            } else {
                // 权限被拒绝，可做相应处理
            }
        }
    }

    /**
     * 下载文件（示例：HttpURLConnection）
     */
    private fun downloadFile(fileUrl: String) {
        println("下载地址="+fileUrl)
        Thread {
            var bis: BufferedInputStream? = null
            var fos: FileOutputStream? = null
            var conn: HttpURLConnection? = null
            try {
                val url = URL(fileUrl)
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 5000
                conn.readTimeout = 5000
                conn.doInput = true
                conn.connect()

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    // 文件名可通过时间戳或链接后缀来区分
                    val fileName =fileUrl+ getFileExtensionFromUrl(fileUrl)
                    val storageDir: File? =
                        getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    if (storageDir != null && !storageDir.exists()) {
                        storageDir.mkdirs()
                    }
                    val outFile = File(storageDir, fileName)

                    bis = BufferedInputStream(conn.inputStream)
                    fos = FileOutputStream(outFile)

                    val buffer = ByteArray(1024)
                    var len: Int
                    while ((bis.read(buffer).also { len = it }) != -1) {
                        fos.write(buffer, 0, len)
                    }
                    fos.flush()
                    // 下载完成后更新轮播列表
                    runOnUiThread(Runnable { initMediaListAndPlay() })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    bis?.close()
                    fos?.close()
                    conn?.disconnect()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }.start()
    }

    /**
     * 根据 url 简单判断文件后缀
     */
    private fun getFileExtensionFromUrl(url: String): String {
        if (url.contains("type=1")) {
            return ".jpg"
        } else if (url.contains("type=2")) {
            return ".mp4"
        }
        return ""
    }

    /**
     * 初始化本地媒体列表，并开始播放
     */
    private fun initMediaListAndPlay() {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (storageDir == null || !storageDir.exists()) {
            return
        }

        val files = storageDir.listFiles()
        if (files == null || files.size == 0) {
            return
        }
        println("有多少个文件="+files.size)
        mediaPathList.clear()
        for (f in files) {
            if (f.name.endsWith(".mp4")
                || f.name.endsWith(".jpg")
                || f.name.endsWith(".png")
            ) {
                mediaPathList.add(f.absolutePath)
            }
        }

        if (mediaPathList.isEmpty()) {
            return
        }

        // 从第 0 个开始播放
        currentIndex = 0
        playMediaAtIndex(currentIndex)
    }

    /**
     * 根据索引播放媒体
     * - 如果是视频，则等视频播完后再切换到下一个
     * - 如果是图片，则展示 10 秒后切换
     */
    private fun playMediaAtIndex(index: Int) {
        println("播放第几个媒体="+index)
        if (mediaPathList.isEmpty()) return
        val path = mediaPathList[index]

        // 先停止之前的播放或回调，避免重复
        mHandler.removeCallbacks(nextMediaRunnable)
        mVideoView!!.stopPlayback()

        if (path.endsWith(".mp4")) {
            // 播放视频
            mImageView!!.visibility = View.GONE
            mVideoView!!.visibility = View.VISIBLE

            mVideoView!!.setVideoURI(Uri.fromFile(File(path)))
            mVideoView!!.start()

            // 视频播放完成后切到下一个媒体
            mVideoView!!.setOnCompletionListener { playNextMedia() }
        } else {
            // 显示图片
            mVideoView!!.visibility = View.GONE
            mImageView!!.visibility = View.VISIBLE

            val bitmap = BitmapFactory.decodeFile(path)
            mImageView!!.setImageBitmap(bitmap)

            // 10 秒后切到下一个媒体
            mHandler.postDelayed(nextMediaRunnable, 5000)
        }
    }

    /**
     * 切换到下一个媒体
     */
    private fun playNextMedia() {
        if (mediaPathList.isEmpty()) return
        currentIndex = (currentIndex + 1) % mediaPathList.size
        playMediaAtIndex(currentIndex)
    }

    protected override fun onDestroy() {
        super.onDestroy()
        // 清理回调，避免内存泄漏
        mHandler.removeCallbacks(nextMediaRunnable)
        mVideoView!!.stopPlayback()
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 100
    }
}