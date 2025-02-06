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
    private val host = "http://192.168.1.14:8008"
    private var randomLinks: MutableList<String> = ArrayList()
    private val mediaPathList: MutableList<String> = ArrayList()
    private var currentIndex = 0
    private val mHandler = Handler(Looper.getMainLooper())
    private val nextMediaRunnable = Runnable { playNextMedia() }
    private var url: String = ""
    private var sp: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mVideoView = findViewById(R.id.video_view)
        mImageView = findViewById(R.id.image_view)
        sp = getSharedPreferences("zhihong_tv", 0)
        url = sp?.getString("url", "").toString()
        val serial = sp?.getString("serial", "")

        if (!serial.isNullOrEmpty()) {
            checkAndRequestPermissions()
        } else {
            showDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun showDialog() {
        val view: View = layoutInflater.inflate(R.layout.half_dialog_view, null)
        val editText = view.findViewById<EditText>(R.id.editTextText)
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("输入唯一序号")
            .setView(view)
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("确定") { dialog, _ ->
                val id = editText.text.toString()
                Toast.makeText(this@MainActivity, id, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                url = "$host/Api/getAll?id=$id"
                sp?.edit()?.putString("url", url)?.apply()
                getUrl(url)
            }.create()
        dialog.show()
    }

    private fun getUrl(url: String) {
        val client = OkHttpClient()
        val request: Request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(String(response.body?.bytes()!!))
                if (json != null && json.getInt("code") == 1) {
                    val data = json.getJSONArray("data")
                    runOnUiThread {
                        mediaPathList.clear()
                        val serial = UUID.randomUUID().toString()
                        sp?.edit()?.putString("serial", serial)?.apply()
                        for (i in 0 until data.length()) {
                            val downloadUrl = "$host/Api/download?path=${data.getJSONObject(i).getString("url")}&serial=$serial&type=${data.getJSONObject(i).getInt("type")}"
                            randomLinks.add(downloadUrl)
                            downloadFile(downloadUrl)
                        }
                        checkLocalFilesAndUpdate()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkAndRequestPermissions() {
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                .setData(Uri.parse("package:${packageName}"))
            startActivity(intent)
        } else {
            initMediaListAndPlay()
        }
    }

    private fun downloadFile(fileUrl: String) {
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
                    // 获取文件名
                    val fileName = fileUrl + getFileExtensionFromUrl(fileUrl)
                    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
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
                    runOnUiThread { initMediaListAndPlay() }
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

    private fun getFileExtensionFromUrl(url: String): String {
        return when {
            url.contains("type=1") -> ".jpg"
            url.contains("type=2") -> ".mp4"
            else -> ""
        }
    }

    private fun initMediaListAndPlay() {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (storageDir == null || !storageDir.exists()) {
            return
        }

        val files = storageDir.listFiles()
        if (files == null || files.isEmpty()) {
            return
        }

        mediaPathList.clear()
        for (f in files) {
            if (f.name.endsWith(".mp4") || f.name.endsWith(".jpg") || f.name.endsWith(".png")) {
                mediaPathList.add(f.absolutePath)
            }
        }

        if (mediaPathList.isEmpty()) {
            return
        }

        currentIndex = 0
        playMediaAtIndex(currentIndex)
    }

    private fun playMediaAtIndex(index: Int) {
        if (mediaPathList.isEmpty()) return
        val path = mediaPathList[index]

        mHandler.removeCallbacks(nextMediaRunnable)
        mVideoView!!.stopPlayback()

        if (path.endsWith(".mp4")) {
            mImageView!!.visibility = View.GONE
            mVideoView!!.visibility = View.VISIBLE
            mVideoView!!.setVideoURI(Uri.fromFile(File(path)))
            mVideoView!!.start()
            mVideoView!!.setOnCompletionListener { playNextMedia() }
        } else {
            mVideoView!!.visibility = View.GONE
            mImageView!!.visibility = View.VISIBLE
            val bitmap = BitmapFactory.decodeFile(path)
            mImageView!!.setImageBitmap(bitmap)
        }

        mHandler.postDelayed(nextMediaRunnable, 5000)
    }

    private fun playNextMedia() {
        currentIndex++
        if (currentIndex >= mediaPathList.size) {
            currentIndex = 0
        }
        playMediaAtIndex(currentIndex)
    }

    private fun checkLocalFilesAndUpdate() {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (storageDir == null || !storageDir.exists()) {
            return
        }

        val files = storageDir.listFiles()
        val fileNamesInLocal = files?.map { it.name } ?: emptyList()

        // 删除本地不在 URL 列表中的文件
        files?.forEach {
            if (!randomLinks.any { url -> it.name in url }) {
                it.delete()
            }
        }

        // 下载 URL 中没有的文件
        randomLinks.forEach { url ->
            val fileName = url.substringAfterLast("=")
            if (fileNamesInLocal.none { it == fileName }) {
                downloadFile(url)
            }
        }
    }
}