package com.ble.newbeaconconfig

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var userID: String? = null
    private var localFilePath = ""

    private val mediaFiles: MutableSet<String> = HashSet()
    private var currentMediaIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private var imageView: ImageView? = null
    private var videoView: VideoView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        imageView = findViewById(R.id.imageView)
        videoView = findViewById(R.id.videoView)

        userID = preferences.getString(KEY_USER_ID, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }

        localFilePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/media"
        if (!File(localFilePath).exists()) {
            File(localFilePath).mkdirs()
        }

        scanLocalFiles() // 启动时先扫描本地文件

        if (userID == null) {
            showIDInputDialog()
        } else {
            fetchDataAndDownload()
        }
    }

    private fun showIDInputDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter ID")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            userID = input.text.toString().trim()
            if (!userID.isNullOrEmpty()) {
                val editor = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit()
                editor.putString(KEY_USER_ID, userID)
                editor.apply()
                fetchDataAndDownload()
            } else {
                Toast.makeText(this, "ID cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        builder.show()
    }

    private fun fetchDataAndDownload() {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("$downloadUrl$userID")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    scanLocalFiles() // 失败时扫描本地文件
                    startPollingFiles()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val json = response.body!!.string()
                    val jsonObject = JSONObject(json)
                    println("API Response: $json")

                    if (jsonObject.getInt("code") == 1) {
                        val data = jsonObject.getJSONArray("data")
                        val validFileNames: MutableSet<String> = HashSet()

                        for (i in 0 until data.length()) {
                            val item = data.getJSONObject(i)
                            val used = item.getInt("used")
                            val url = item.getString("url")
                            val type = item.getInt("type")
                            val fileName = url + if (type == 1) ".jpg" else ".mp4"

                            if (used == 1) {
                                validFileNames.add(fileName)
                                val file = File(localFilePath, fileName)
                                if (!file.exists()) {
                                    println("需要下载")
                                    downloadFile(url, fileName, type)
                                }
                                mediaFiles.add(file.path)
                            } else {
                                val file = File(localFilePath, fileName)
                                if (file.exists()) {
                                    file.delete()
                                }
                            }
                        }

                        deleteInvalidFiles(validFileNames)
                        startPollingFiles()
                    } else {
                        scanLocalFiles()
                        startPollingFiles()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to parse data", Toast.LENGTH_SHORT).show()
                    }
                    scanLocalFiles()
                    startPollingFiles()
                }
            }
        })
    }

    private fun downloadFile(url: String, fileName: String, type: Int) {
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
        var serial = preferences.getString("serial", null);
        if (serial.isNullOrEmpty()) {
            serial = UUID.randomUUID().toString()
            preferences.edit().putString("serial", serial).apply()
        }
        val downloadUrl = "http://192.168.1.14:8008/Api/download?path=$url&type=$type&serial=$serial"

        val client = OkHttpClient()
        val request = Request.Builder().url(downloadUrl).build()
        println("下载的文件地址=" + downloadUrl)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to download file", Toast.LENGTH_SHORT).show()
                    scanLocalFiles()
                    startPollingFiles()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val file = File(localFilePath, fileName)
                    FileOutputStream(file).use { fos ->
                        val buffer = ByteArray(1024)
                        val inputStream = response.body!!.byteStream()
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            fos.write(buffer, 0, len)
                        }
                        fos.flush()
                    }
                    println("下载完成=" + file.path)
                    println("run=" + run)
                    mediaFiles.add(file.path)
                    if(run==0||mediaFiles.size==1){
                        println("启动播放")
                        startPollingFiles()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun scanLocalFiles() {
        val directory = File(localFilePath)
        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles { file ->
                file.extension.equals("jpg", ignoreCase = true) ||
                        file.extension.equals("mp4", ignoreCase = true)
            }
            files?.forEach { file ->
                mediaFiles.add(file.path)
                println("本地问价名=" + file.path)
            }
        }
    }

    private fun deleteInvalidFiles(validFileNames: Set<String>) {
        val directory = File(localFilePath)
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (!validFileNames.contains(file.name)) {
                    println("Deleting invalid file: ${file.name}")
                    file.delete()
                }
            }
        }
    }
var run=0;
    private fun startPollingFiles() {
            runOnUiThread {

                if (mediaFiles.isNotEmpty()) {

                    println("资源长度="+mediaFiles.size)
                    val currentFile = mediaFiles.elementAt(currentMediaIndex)
                    displayMedia(currentFile)
                    currentMediaIndex = (currentMediaIndex + 1) % mediaFiles.size

            } }


    }

    private fun displayMedia(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            run=1;
            val fileExtension = file.extension
            if (fileExtension.equals("jpg", ignoreCase = true)) {
                imageView!!.visibility = View.VISIBLE
                videoView!!.visibility = View.GONE
                imageView!!.setImageBitmap(BitmapFactory.decodeFile(file.path))
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        startPollingFiles()
                    }
                },5000)

            } else if (fileExtension.equals("mp4", ignoreCase = true)) {
                imageView!!.visibility = View.GONE
                videoView!!.visibility = View.VISIBLE
                videoView!!.setVideoURI(Uri.fromFile(file))
                videoView!!.start()

                videoView!!.setOnCompletionListener {
                    // 视频播放完毕后切换到下一张图片
                    startPollingFiles()
                }
            }
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "AppPreferences"
        private const val KEY_USER_ID = "userId"
        private const val downloadUrl = "http://192.168.1.14:8008/Api/getAll?id="
    }
var firstTime:Long?=0;
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                var secondTime = System.currentTimeMillis();
                if (secondTime- firstTime!! > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime
                    return true;
                } else {
                    finish(); // 调用finish()方法，结束当前Activity
                    System.exit(0); // 强制退出应用
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }



}