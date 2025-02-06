package com.ble.view_sw

/**
 * @package com.ble.view
 * @fileName PopupWindowCakkBack
 * @date 2020/6/2916:47
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface MyFilterViewCallBack {

        fun onNameOrMac(content: String)
        fun onRawData(content: String)
        fun onRssi(rssi:Int)
        fun onScan()
        fun onchange()

}