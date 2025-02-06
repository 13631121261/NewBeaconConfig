package com.ble.model

/**
 * @package com.ble.model
 * @fileName ConnectDataCallBack
 * @date 2020/7/411:51
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface ConnectDataCallBack {
    fun onData1(data:ByteArray);
    fun onData(data:String);
    fun onDataAcc(data:ByteArray);
    fun onDataTemp(data:ByteArray);
    fun onDisConnected(address:String);
    fun onConnected(address:String)
    fun onDataVersion(data:ByteArray);
}