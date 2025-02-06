package com.ble.presenter

/**
 * @package com.ble.presenter
 * @fileName IMainPresenter
 * @date 2020/6/1610:37
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface IMainPresenter {
    fun startScan();
    fun stopScan();
    fun startScan(timeout:Int);
}