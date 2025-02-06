package com.ble.view_sw

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import com.ble.beaconconfig.R
import com.ble.bean.Type

class Pop_password(mContext: Activity, itemsOnClick: View.OnClickListener?) : PopupWindow() {
    private val mContext: Context
    private val view: View
    private val ed_password: EditText
    private val btn_save_pop: Button
    private var type: Type ?=null;
    private var txt_title: TextView
    private lateinit var spinner:Spinner
    var adapter_dbm:Adapter?=null
    var adapter_rssione:Adapter?=null
    var adapter_interval:Adapter?=null
    var dbm: Byte?=null;
    var sdBm:String?=null
    var b_rssi:String="";
    var measure:Byte=0;
    var adv: Byte?=null;
    var sAdv:String?=null
    init {
        this.mContext = mContext
        view = LayoutInflater.from(mContext).inflate(R.layout.pop_password, null)
        ButterKnife.bind(mContext)
        ed_password = view.findViewById<View>(R.id.text_name) as EditText
        btn_save_pop = view.findViewById<View>(R.id.btn_save_pop) as Button
        txt_title=view.findViewById<View>(R.id.txt_title) as TextView
        spinner=view.findViewById<Spinner>(R.id.spinner_dbm) as Spinner

        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick)

        // 设置外部可点击
        this.isOutsideTouchable = true

        /* 设置弹出窗口特征 */
        // 设置视图
        this.contentView = view

        // 设置弹出窗体的宽和高
        /*
                 * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
                 * 对象,这样这可以以同样的方式改变这个Activity的属性.
                 */
        val dialogWindow = mContext.window
        val m = mContext.windowManager
        val d = m.defaultDisplay // 获取屏幕宽、高用
        val p = dialogWindow.attributes // 获取对话框当前的参数值
        this.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        this.width = (d.width * 0.8).toInt()


        // 设置弹出窗体可点击
        this.isFocusable = true
        adapter_dbm = ArrayAdapter.createFromResource(
                mContext, R.array.spinner_dbm,
                android.R.layout.simple_spinner_item)
        adapter_rssione= ArrayAdapter.createFromResource(
                mContext, R.array.spinner_measure,
                android.R.layout.simple_spinner_item)
        adapter_interval=ArrayAdapter.createFromResource(
                mContext, R.array.spinner_adv_0nterval,
                android.R.layout.simple_spinner_item)
        spinner.adapter= adapter_dbm as ArrayAdapter<CharSequence>  // this will set list of values to spinner



        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (type == Type.txpower) {
                    when (position) {
                        0 -> {
                            dbm = 0x01
                            sdBm = "4"
                        }
                        1 -> {
                            dbm = 0x02
                            sdBm = "0"
                        }
                        2 -> {
                            dbm = 0x03
                            sdBm = "-4"
                        }
                        3 -> {
                            sdBm = "-8"
                            dbm = 0x04
                        }
                        4 -> {
                            sdBm = "-12"
                            dbm = 0x05
                        }
                        5 -> {
                            sdBm = "-16"
                            dbm = 0x06
                        }
                        6 -> {
                            sdBm = "-20"
                            dbm = 0x07
                        }
                        7 -> {
                            sdBm = "-40"
                            dbm = 0x08
                        }
                    }
                } else if (type == Type.rssiatone) {
                    b_rssi = parent.adapter.getItem(position) as String
                    println("选中$b_rssi")
                    measure = b_rssi.toInt().toByte()
                } else if (type == Type.interval) {
                    when (position) {
                        0 -> {
                            adv = 0x01
                            sAdv = "100"
                        }
                        1 -> {
                            sAdv = "200"
                            adv = 0x02
                        }
                        2 -> {
                            sAdv = "300"
                            adv = 0x03
                        }
                        3 -> {
                            sAdv = "400"
                            adv = 0x04
                        }
                        4 -> {
                            sAdv = "500"
                            adv = 0x05
                        }
                        5 -> {
                            sAdv = "600"
                            adv = 0x06
                        }
                        6 -> {
                            sAdv = "700"
                            adv = 0x07
                        }
                        7 -> {
                            sAdv = "800"
                            adv = 0x08
                        }
                        8 -> {
                            sAdv = "900"
                            adv = 0x09
                        }
                        9 -> {
                            sAdv = "1000"
                            adv = 0x0A
                        }
                        10 -> {
                            sAdv = "1500"
                            adv = 0x0B
                        }
                        11 -> {
                            sAdv = "2000"
                            adv = 0x0C
                        }
                        12 -> {
                            sAdv = "2500"
                            adv = 0x0D
                        }
                        13 -> {
                            sAdv = "3000"
                            adv = 0x0E
                        }
                        14 -> {
                            sAdv = "5000"
                            adv = 0x0F
                        }
                        15 -> {
                            sAdv = "10000"
                            adv = 0x10
                        }

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        }
    fun setType(type: Type,string: String){
        this.type=type;
        when(type){
            Type.psw -> {
                txt_title?.text = mContext.resources.getString(R.string.check_password)
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
            }
            Type.change_psw -> {
                txt_title?.text = mContext.resources.getString(R.string.change_password)
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
            }
            Type.deviceName -> {
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
                txt_title?.text = mContext.resources.getString(R.string.setName)
                System.out.println("输出类型=$type" + txt_title)
            }
            Type.uuid -> {
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
                txt_title?.text = mContext.resources.getString(R.string.setUUID)
            }
            Type.serviceData -> {
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
                txt_title?.text = mContext.resources.getString(R.string.setCustomer)
            }
            Type.minor -> {
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
                txt_title?.text = mContext.resources.getString(R.string.setMinor)
            }
            Type.major -> {
                ed_password.visibility = View.VISIBLE;
                spinner.visibility = View.GONE
                txt_title?.text = mContext.resources.getString(R.string.setMajor)
            }
            Type.txpower -> {
                ed_password.visibility = View.GONE;
                spinner.visibility = View.VISIBLE
                txt_title?.text = mContext.resources.getString(R.string.setTxPower)
                spinner.adapter = adapter_dbm as ArrayAdapter<CharSequence>  // this will set list of values to spinne
                val apsAdapter = spinner.adapter //得到SpinnerAdapter对象
                val k = apsAdapter.count
                Log.e("log=发射功率",string)
                for (i in 0 until k) {
                    if (string.equals(apsAdapter.getItem(i).toString())) {
                        spinner.setSelection(i, true) // 默认选中项
                        break
                    }
                }
            }
            Type.rssiatone -> {
                ed_password.visibility = View.GONE;
                spinner.visibility = View.VISIBLE
                txt_title?.text = mContext.resources.getString(R.string.setRssiAt1m)
                spinner.adapter = adapter_rssione as ArrayAdapter<CharSequence>  // this will set list of values to spinne
                val apsAdapter = spinner.adapter //得到SpinnerAdapter对象
                val k = apsAdapter.count
                var rssi=string.split(" ");
                Log.e("log=rssi",rssi[0])
                for (i in 0 until k) {
                    if (rssi[0].equals(apsAdapter.getItem(i).toString())) {
                        spinner.setSelection(i, true) // 默认选中项
                        break
                    }
                }
            }
            Type.interval -> {
                ed_password.visibility = View.GONE;
                spinner.visibility = View.VISIBLE
                txt_title?.text = mContext.resources.getString(R.string.setAdv)
                spinner.adapter = adapter_interval as ArrayAdapter<CharSequence>  // this will set list of values to spinne
                val apsAdapter = spinner.adapter //得到SpinnerAdapter对象
                val k = apsAdapter.count
                var rssi=string.split(" ");
                Log.e("log=广播间隔",rssi[0])
                for (i in 0 until k) {
                    if (string.equals(apsAdapter.getItem(i).toString())) {
                        spinner.setSelection(i, true) // 默认选中项
                        break
                    }
                }
            }

            else -> {}
        }

    }
    fun setEmptry(){
        ed_password.setText("");
    }
    fun setText(string: String){
        ed_password.setText(string.replace(" ", ""));
    }

    fun   getText():String{
        return ed_password.text.toString();
    }
    fun getType(): Type? {
        return type;
    }

    fun getdBm(): Byte? {
        return dbm;
    }
    fun getdBms(): String? {
        return sdBm;
    }
    fun getb_rssi():String{
        return b_rssi
    }
    @JvmName("getMeasure1")
    fun getMeasure():Byte{
        return measure
    }
    override fun dismiss(){
        super.dismiss()
        ed_password.setText("")
    }

    @JvmName("getAdv1")
    fun getAdv(): Byte? {
        return adv;
    }
    fun getADVS(): String? {
        return sAdv;
    }

}