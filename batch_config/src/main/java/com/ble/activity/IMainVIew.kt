package com.ble.activity

import com.ble.bean.Beacon

/**
 * @package com.ble.view
 * @fileName IMainVIew
 * @date 2020/6/1610:47
 * @author Andesen
 * @email 929620555@qq.com
 * @describe 自定义描述
 */
interface IMainVIew {
    fun showBeacon(list: ArrayList<Beacon>);
    fun showBeacon(beacon: Beacon);
    fun showTip();
    fun toSelectFile();

}