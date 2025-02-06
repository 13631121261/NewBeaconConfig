package com.ble.activity

/**
 * @package com.ble.activity
 * @fileName IBeaconDetailView
 * @date 2020/7/416:25
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface IBeaconDetailView {
    fun show(tip:String)
    fun showPasswordDialog()
    fun connectOk();
    fun connectFail();
    //密码错误,导致主动断开
    fun connectDis();
    fun setAcc(data: ByteArray);
    fun setTemp(data: ByteArray);
    fun showVersion(version:String)
    fun showData(data: ByteArray)
}