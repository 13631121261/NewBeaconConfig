package com.ble.presenter

/**
 * @package com.ble.model
 * @fileName IConnect
 * @date 2020/7/315:38
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface IConnectPresenter {
    fun connect(address:String)
    fun disconnect()
    fun sendPassword(psw:String)
    fun sendChangePassword(psw:String)

    fun close();

}