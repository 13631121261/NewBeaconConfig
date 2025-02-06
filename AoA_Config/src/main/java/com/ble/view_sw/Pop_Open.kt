package com.ble.view_sw

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import com.ble.aoaconfig.R
import com.ble.bean.Type

class Pop_Open(mContext: Activity, itemsOnClick: View.OnClickListener?) : PopupWindow() , CompoundButton.OnCheckedChangeListener {
    private val mContext: Context
    private val view: View
    private val btn_save_pop: Button
    private var type: Type ?=null;
    private var txt_title: TextView
    private lateinit var spinner:Spinner
    var adapter_adv:Adapter?=null
    var adapter_check:Adapter?=null
    var check=0

    var support_acc:Boolean=false
    var support_wenshidu:Boolean=false



    var accs=0
    var switch_acc:Switch?=null
    init {
        this.mContext = mContext
        view = LayoutInflater.from(mContext).inflate(R.layout.pop_acc_temp, null)
        ButterKnife.bind(mContext)
        btn_save_pop = view.findViewById<View>(R.id.btn_save_pop) as Button
        txt_title=view.findViewById<View>(R.id.txt_title) as TextView
        spinner=view.findViewById<Spinner>(R.id.spinner_adv) as Spinner
        switch_acc=view.findViewById(R.id.switch_acc)as Switch

        // 设置按钮监听
        btn_save_pop.setOnClickListener(itemsOnClick)
        switch_acc?.setOnCheckedChangeListener(this)
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
        adapter_adv = ArrayAdapter.createFromResource(
                mContext, R.array.spinner_adv,
                android.R.layout.simple_spinner_item)
        adapter_check=ArrayAdapter.createFromResource(
                mContext, R.array.spinner_check,
                android.R.layout.simple_spinner_item)
        spinner.adapter= adapter_adv as ArrayAdapter<CharSequence>  // this will set list of values to spinner

        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (type == Type.wendu_Open) {
                    when (position) {
                        0 -> {
                            check=3
                        }
                        1 -> {
                            check=5
                        }
                        2 -> {
                            check=10
                        }
                        3 -> {
                            check=15
                        }
                        4 -> {
                            check=30
                        }
                        5 -> {
                            check=50
                        }
                        6 -> {
                            check=100
                        }
                        7 -> {
                            check=200
                        }
                        8 -> {
                            check=300
                        }

                    }

                } else if (type == Type.ACC_Open) {
                    when (position) {
                        0 -> {

                            accs = 20
                        }
                        1 -> {
                            accs = 30

                        }
                        2 -> {
                            accs = 50

                        }
                        3 -> {

                            accs = 100
                        }
                        4 -> {
                            accs = 200

                        }
                        5 -> {
                            accs = 300

                        }
                        6 -> {
                            accs = 400

                        }
                        7 -> {
                            accs = 500

                        }
                        8 -> {
                            accs = 600

                        }
                        9 -> {
                            accs = 700

                        }
                        10 -> {
                            accs = 800

                        }
                        11 -> {
                            accs = 900

                        }
                        12 -> {
                            accs = 1000
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        }
    fun setType(type: Type,b:Boolean){
        this.type=type;
        switch_acc?.isChecked=b
        when(type){

            Type.ACC_Open -> {

                spinner.visibility = View.VISIBLE
                txt_title?.text = mContext.resources.getString(R.string.interval_str)
                spinner.adapter = adapter_adv as ArrayAdapter<CharSequence>  // this will set list of values to spinne
            }
            Type.wendu_Open -> {
                spinner.visibility = View.VISIBLE
                txt_title?.text = mContext.resources.getString(R.string.temp_str)
                spinner.adapter = adapter_check as ArrayAdapter<CharSequence>  // this will set list of values to spinne
            }

            else -> {}
        }

    }

    fun getType(): Type? {
        return type;
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
       // TODO("Not yet implemented")
      //  super(p0,p1);
       if(Type.wendu_Open==type){
           support_wenshidu=p1
       }else{
           support_acc=p1
       }

    }


}